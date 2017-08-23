package com.bakigoal.service;

import com.bakigoal.model.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

  @PersistenceContext
  private EntityManager em;

  public Employee createEmployee(Employee employee) {
    em.persist(employee);
    return employee;
  }

  public void removeEmployee(long id) {
    Employee emp = findEmployee(id);
    if (emp != null) {
      em.remove(emp);
    }
  }

  public void update(Employee employee) {
    em.merge(employee);
  }

  public Employee findEmployee(long id) {
    return em.find(Employee.class, id);
  }

  public List<Employee> findAllEmployees() {
    TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
    return query.getResultList();
  }


}
