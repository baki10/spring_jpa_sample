package com.bakigoal.service;

import com.bakigoal.model.Department;
import com.bakigoal.model.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Query Best Practices
 * <p>
 * 1) First and foremost, we recommend named queries whenever possible
 * 2) If you are executing queries that return entities for reporting purposes and have no intention of modifying
 * the results, consider executing queries using a transaction-scoped entity manager but outside of a transaction
 */
@Service
@Transactional
public class QueryService {

  private static final String QUERY =
      "SELECT e.salary FROM Employee e WHERE e.department.name = :deptName AND " + "e.lastName = :empName ";

  @PersistenceContext
  private EntityManager em;

  @PersistenceUnit
  private EntityManagerFactory emf;

  /**
   * A dynamic query can be turned into a named query by using the EntityManagerFactory addNamedQuery()
   */
  @PostConstruct
  public void init() {
    TypedQuery<Long> q = em.createQuery(QUERY, Long.class);
    emf.addNamedQuery("findSalaryForNameAndDepartment", q);
  }

  /**
   * Because the names are concatenated into the string instead of using parameter binding,
   * it is effectively creating a new and unique query each time.
   * 100 calls to this method could potentially generate 100 different query strings.
   * The second problem with this example is that it is vulnerable to injection attacks
   */
  public long queryEmpSalary(String deptName, String empName) {
    String query = "SELECT e.salary " +
        "FROM Employee e " +
        "WHERE e.department.name = '" + deptName +
        "' AND " + "e.lastName = '" + empName + "' ";
    return em.createQuery(query, Long.class).getSingleResult();
  }

  /**
   * This example reduces the number of unique queries parsed by the query engine.
   * And defeats the security threat described previously
   * If dynamic queries are a necessity, take care to use parameter binding instead of concatenating parameter values
   * into query strings in order to minimize the number of distinct query strings parsed by the query engine
   */
  public long queryEmpSalary2(String deptName, String empName) {
    return em.createQuery(QUERY, Long.class)
        .setParameter("deptName", deptName)
        .setParameter("empName", empName)
        .getSingleResult();
  }

  public long queryEmpSalary3(String deptName, String empName) {
    return em.createNamedQuery("findSalaryForNameAndDepartment", Long.class)
        .setParameter("deptName", deptName)
        .setParameter("empName", empName)
        .getSingleResult();
  }

  /**
   * using the createNamedQuery() call to create and execute a named query that requires a query parameter
   */
  public List<Employee> findEmployeeByName(String name) {
    return em.createNamedQuery("Employee.findByName", Employee.class)
        .setParameter("name", name)
        .getResultList();
  }

  public List<Employee> findEmployeesHiredDuringPeriod(Date start, Date end) {
    return em.createQuery(
        "SELECT e " +
            "FROM Employee e " +
            "WHERE e.startDate BETWEEN ?1 AND ?2", Employee.class)
        .setParameter(1, start, TemporalType.DATE)
        .setParameter(2, end, TemporalType.DATE)
        .getResultList();
  }

  /**
   * Whereas getResultList() returns an empty collection when no results are available,
   * getSingleResult() throws a NoResultException exception
   * If multiple results are available after executing the query instead of the single expected result,
   * getSingleResult() will throw a NonUniqueResultException exception
   */
  public void displayProjectEmployees(String projectName) {
    List<Employee> result = em.createQuery(
        "SELECT e " +
            "FROM Project p JOIN p.employees e " +
            "WHERE p.name = ?1 " +
            "ORDER BY e.lastName", Employee.class)
        .setParameter(1, projectName)
        .getResultList();
    int count = 0;
    for (Employee e : result) {
      System.out.println(++count + ": " + e.getLastName() + ", " + e.getDepartment().getName());
    }

  }

  @Transactional(readOnly = true)
  public List<Department> findAllDepartmentsDetached() {
    return em.createQuery("SELECT d FROM Department d", Department.class)
        .getResultList();
  }

  public void displayProjectEmployees2(String projectName) {
    List result = em.createQuery(
        "SELECT e.name, e.department.name " +
            "FROM Project p JOIN p.employees e " +
            "WHERE p.name = ?1 " +
            "ORDER BY e.lastName")
        .setParameter(1, projectName)
        .getResultList();
    int count = 0;
    for (Object aResult : result) {
      Object[] values = (Object[]) aResult;
      System.out.println(++count + ": " + values[0] + ", " + values[1]);
    }
  }


}
