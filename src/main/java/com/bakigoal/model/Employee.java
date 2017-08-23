package com.bakigoal.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String name;
  private long salary;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] picture;
  // relationships starts ----------------
  @ManyToOne
  @JoinColumn(name = "DEPT_ID")
  private Department department;
  @OneToOne
  private ParkingSpace parkingSpace;
  @ManyToMany
  @JoinTable(name = "EMP_PROJ",
      joinColumns = @JoinColumn(name = "EMP_ID"),
      inverseJoinColumns = @JoinColumn(name = "PROJ_ID"))
  private List<Project> projects;
  @OneToMany
  @JoinTable(name = "EMP_PHONE",
      joinColumns = @JoinColumn(name = "EMP_ID"),
      inverseJoinColumns = @JoinColumn(name = "PHONE_ID"))
  private List<Phone> phones;

  // relationships ends ------------------


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

  public long getSalary() {
    return salary;
  }

  public void setSalary(long salary) {
    this.salary = salary;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public ParkingSpace getParkingSpace() {
    return parkingSpace;
  }

  public void setParkingSpace(ParkingSpace parkingSpace) {
    this.parkingSpace = parkingSpace;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  public List<Phone> getPhones() {
    return phones;
  }

  public void setPhones(List<Phone> phones) {
    this.phones = phones;
  }
}

