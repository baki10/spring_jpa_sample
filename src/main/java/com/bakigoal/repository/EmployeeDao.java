package com.bakigoal.repository;

import com.bakigoal.model.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class EmployeeDao {

  @PersistenceContext
  private EntityManager em;

  public Employee createEmployee(Employee employee) {
    em.persist(employee);
    return employee;
  }

  public void removeEmployee(int id) {
    Employee emp = findEmployee(id);
    if (emp != null) {
      em.remove(emp);
    }
  }

  public void update(Employee employee) {
    em.merge(employee);
  }

  public Employee findEmployee(int id) {
    return em.find(Employee.class, id);
  }

  public List<Employee> findAllEmployees() {
    TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
    return query.getResultList();
  }


}
