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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SearchService {

  @PersistenceContext
  private EntityManager em;

  public List<Employee> pathExpressions(){
   String query =
       "SELECT e " +
       "FROM Employee e " +
       "WHERE e.address.city = 'New York'";
    return em.createQuery(query, Employee.class).getResultList();
  }

  public List<Employee> pathExpressionsCriteriaAPI(){
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
    Root<Employee> emp = query.from(Employee.class);
    query.select(emp)
        .where(builder.equal(emp.get("address").get("city"), "New York"));

    TypedQuery<Employee> departmentTypedQuery = em.createQuery(query);
    return departmentTypedQuery.getResultList();
  }

  public List<Department> findDepartments(){
    String query =
        "SELECT DISTINCT d " +
        "FROM Department d, Employee e " +
        "WHERE d = e.department";
    return em.createQuery(query, Department.class).getResultList();
  }

  public List<Department> findDepartmentsCriteriaAPI(){
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

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);
    Root<Employee> emp = cq.from(Employee.class);
    cq.select(emp);
    cq.distinct(true);
    Join<Employee, Project> project = emp.join("projects", JoinType.LEFT);

    List<Predicate> criteria = new ArrayList<>();
    if (name != null) {
      criteria.add(builder.equal(emp.get("name"), builder.parameter(String.class, "name")));
    }
    if (deptName != null) {
      criteria.add(builder.equal(emp.get("dept").get("name"), builder.parameter(String.class, "dept")));
    }
    if (projectName != null) {
      criteria.add(builder.equal(project.get("name"), builder.parameter(String.class, "project")));
    }
    if (city != null) {
      criteria.add(builder.equal(emp.get("address").get("city"), builder.parameter(String.class, "city")));
    }

    if (criteria.size() == 0) {
      throw new RuntimeException("no criteria");
    } else if (criteria.size() == 1) {
      cq.where(criteria.get(0));
    } else {
      cq.where(builder.and(criteria.toArray(new Predicate[0])));
    }

    TypedQuery<Employee> q = em.createQuery(cq);
    if (name != null) {
      q.setParameter("name", name);
    }
    if (deptName != null) {
      q.setParameter("dept", deptName);
    }
    if (project != null) {
      q.setParameter("project", projectName);
    }
    if (city != null) {
      q.setParameter("city", city);
    }
    return q.getResultList();
  }

}
