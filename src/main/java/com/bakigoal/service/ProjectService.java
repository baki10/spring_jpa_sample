package com.bakigoal.service;

import com.bakigoal.model.Employee;
import com.bakigoal.model.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class ProjectService {

  @PersistenceContext
  private EntityManager em;

  public void assignEmployeeToProject(int empId, int projectId) {
    Project project = em.find(Project.class, projectId);  //  the container checks for a transaction
    Employee employee = em.find(Employee.class, empId);
    // At this point, employee and project are both managed entity instances
    project.getEmployees().add(employee);
    employee.getProjects().add(project);
    // When the method call ends, the transaction is committed
  }
  // Because the employee and project instances were managed,
  // the persistence context can detect any state changes in them,
  // and it updates the database during the commit

}
