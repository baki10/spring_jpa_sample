package com.bakigoal.service;

import com.bakigoal.model.Department;
import com.bakigoal.model.Employee;
import com.bakigoal.model.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SearchService {

  @PersistenceContext
  private EntityManager em;

  public List<Employee> subQuery(String projectName) {
    String query = "SELECT e " +
        "FROM Employee e " +
        "WHERE EXISTS " +
        "(SELECT p FROM e.projects p WHERE p.name = :name)";
    return em.createQuery(query, Employee.class).getResultList();
  }

  public List<Employee> subQueryCriteriaAPI(String projectName) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
    Root<Employee> emp = query.from(Employee.class);

    Subquery<Project> sq = query.subquery(Project.class);
    Root<Employee> sqEmp = sq.correlate(emp);
    Join<Employee,Project> project = sqEmp.join("projects");
    sq.select(project)
        .where(builder.equal(project.get("name"), builder.parameter(String.class,"project")));
    Predicate exists = builder.exists(sq);

    query.select(emp)
        .where(exists);

    TypedQuery<Employee> departmentTypedQuery = em.createQuery(query);
    return departmentTypedQuery.getResultList();
  }

  public List<Employee> fetch() {
    String query = "SELECT e " +
        "FROM Employee e JOIN FETCH e.phones";
    return em.createQuery(query, Employee.class).getResultList();
  }

  public List<Employee> fetchCriteriaAPI() {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
    Root<Employee> emp = query.from(Employee.class);
    emp.fetch("phones", JoinType.LEFT);
    query.select(emp);

    TypedQuery<Employee> departmentTypedQuery = em.createQuery(query);
    return departmentTypedQuery.getResultList();
  }

  public List<Employee> pathExpressions() {
    String query = "SELECT e " +
        "FROM Employee e " +
        "WHERE e.address.city = 'New York'";
    return em.createQuery(query, Employee.class).getResultList();
  }

  public List<Employee> pathExpressionsCriteriaAPI() {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
    Root<Employee> emp = query.from(Employee.class);
    query.select(emp)
        .where(builder.equal(emp.get("address").get("city"), "New York"));

    TypedQuery<Employee> departmentTypedQuery = em.createQuery(query);
    return departmentTypedQuery.getResultList();
  }

  public List<Department> findDepartments() {
    String query = "SELECT DISTINCT d " +
        "FROM Department d, Employee e " +
        "WHERE d = e.department";
    return em.createQuery(query, Department.class).getResultList();
  }

  public List<Department> findDepartmentsCriteriaAPI() {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Department> query = builder.createQuery(Department.class);
    // Always store the root objects locally and refer to them when necessary
    Root<Employee> emp = query.from(Employee.class);
    Root<Department> dept = query.from(Department.class);

    query.select(dept)
        .distinct(true)
        .where(builder.equal(dept, emp.get("department")));

    TypedQuery<Department> departmentTypedQuery = em.createQuery(query);
    return departmentTypedQuery.getResultList();
  }

  public List<Employee> findEmployees(String name, String deptName,
                                      String projectName, String city) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT DISTINCT e ");
    query.append("FROM Employee e LEFT JOIN e.projects p ");
    query.append("WHERE ");
    List<String> criteria = new ArrayList<>();
    if (name != null) {
      criteria.add("e.name = :name");
    }
    if (deptName != null) {
      criteria.add("e.dept.name = :dept");
    }
    if (projectName != null) {
      criteria.add("p.name = :project");
    }
    if (city != null) {
      criteria.add("e.address.city = :city");
    }
    if (criteria.size() == 0) {
      throw new RuntimeException("no criteria");
    }
    for (int i = 0; i < criteria.size(); i++) {
      if (i > 0) {
        query.append(" AND ");
      }
      query.append(criteria.get(i));
    }
    TypedQuery<Employee> q = em.createQuery(query.toString(), Employee.class);
    if (name != null) {
      q.setParameter("name", name);
    }
    if (deptName != null) {
      q.setParameter("dept", deptName);
    }
    if (projectName != null) {
      q.setParameter("project", projectName);
    }
    if (city != null) {
      q.setParameter("city", city);
    }
    return q.getResultList();
  }

  public List<Employee> findEmployeesCriteriaAPI(String name, String deptName,
                                                 String projectName, String city) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Employee> c = cb.createQuery(Employee.class);
    Root<Employee> emp = c.from(Employee.class);
    c.select(emp);
    c.distinct(true);

    List<Predicate> criteria = new ArrayList<>();
    if (name != null) {
      criteria.add(cb.equal(emp.get("name"), cb.parameter(String.class, "name")));
    }
    if (deptName != null) {
      criteria.add(cb.equal(emp.get("dept").get("name"), cb.parameter(String.class, "dept")));
    }
    if (projectName != null) {
      //Join<Employee, Project> project = emp.join("projects", JoinType.LEFT);
      //criteria.add(cb.equal(project.get("name"), cb.parameter(String.class, "project")));
      Subquery<Employee> sq = c.subquery(Employee.class);
      Root<Project> project = sq.from(Project.class);
      Join<Project, Employee> sqEmp = project.join("employees");
      sq.select(sqEmp)
          .where(cb.equal(project.get("name"), cb.parameter(String.class, "project")));
      criteria.add(cb.in(emp).value(sq));

    }
    if (city != null) {
      criteria.add(cb.equal(emp.get("address").get("city"), cb.parameter(String.class, "city")));
    }

    if (criteria.size() == 0) {
      throw new RuntimeException("no criteria");
    } else if (criteria.size() == 1) {
      c.where(criteria.get(0));
    } else {
      c.where(cb.and(criteria.toArray(new Predicate[0])));
    }

    TypedQuery<Employee> q = em.createQuery(c);
    if (name != null) {
      q.setParameter("name", name);
    }
    if (deptName != null) {
      q.setParameter("dept", deptName);
    }
    if (projectName != null) {
      q.setParameter("project", projectName);
    }
    if (city != null) {
      q.setParameter("city", city);
    }
    return q.getResultList();
  }

}
