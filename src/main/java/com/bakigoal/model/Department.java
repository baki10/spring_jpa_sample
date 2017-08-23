package com.bakigoal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;
import java.util.Map;

@Entity
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String name;

  @OneToMany(mappedBy = "department")
  @OrderBy("name ASC")
  private List<Employee> employees;

  @OneToMany(mappedBy = "department")
  @MapKey(name = "id")
  private Map<Integer, Employee> employeeMap;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }

  public Map<Integer, Employee> getEmployeeMap() {
    return employeeMap;
  }

  public void setEmployeeMap(Map<Integer, Employee> employeeMap) {
    this.employeeMap = employeeMap;
  }
}
