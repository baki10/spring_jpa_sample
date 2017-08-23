package com.bakigoal.service;

import com.bakigoal.model.Department;
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

  /**
   * The persist() operation is intended for new entities that do not already exist in the database.
   * The persist() method accepts a new entity instance and causes it to become 'managed'.
   * If it is already 'managed' by the persistence context, it is ignored.
   * Be 'managed' does not mean that it is persisted to the DB right away, typically only when the transaction commits.
   */
  public Employee createEmployee(Employee employee) {
    em.persist(employee);
    return employee;
  }

  /**
   * An interesting thing about this example is that the Department is a passive participant despite the Employee
   * instance being added to its collection.
   * The Employee entity is the owner of the relationship because it is in a Many-To-One relationship with the Department.
   * When the Employee is persisted, the foreign key to the Department is written out to the table mapped by the Employee,
   * and no actual change is made to the Department entity’s physical representation.
   * Had we only added the employee to the collection and not updated the other side of
   * the relationship, nothing would have been persisted to the database.
   */
  public void addEmployeeToDepartment(long deptId) {
    // retrieving a pre-existing Department instance
    Department department = em.find(Department.class, deptId);

    // A new Employee instance is then created
    Employee employee = new Employee();
    employee.setFirstName("Peter");
    employee.setLastName("Pan");
    // We then assign the employee to the department
    employee.setDepartment(department);
    // Because the relationship is bidirectional,
    // we then add the new Employee instance to the employees collection
    department.getEmployees().add(employee);

    // Finally the new Employee instance is persisted
    em.persist(employee);

    // Assuming a transaction then commits, and the persistence context is synchronized to it,
    // the new entity will be stored in the database
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
