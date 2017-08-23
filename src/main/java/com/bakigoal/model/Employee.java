package com.bakigoal.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
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
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "state", column = @Column(name = "PROVINCE")),
      @AttributeOverride(name = "zip", column = @Column(name = "POSTAL_CODE"))
  })
  private Address address;

  // relationships starts ----------------

  @ManyToOne
  @JoinColumn(name = "DEPT_ID")
  private Department department;

  @OneToOne(fetch = FetchType.LAZY)  // overriding the default fetch mode
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

  @ElementCollection
  @CollectionTable(name = "VACATION", joinColumns = @JoinColumn(name = "EMP_ID"))
  @AttributeOverride(name = "daysTaken", column = @Column(name = "DAYS_ABS"))
  private Collection<VacationEntry> vacations;

  @ElementCollection
  @Column(name = "NICKNAME")
  private Set<String> nickNames;


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

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
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

  public Collection getVacations() {
    return vacations;
  }

  public void setVacations(Collection vacations) {
    this.vacations = vacations;
  }

  public Set<String> getNickNames() {
    return nickNames;
  }

  public void setNickNames(Set<String> nickNames) {
    this.nickNames = nickNames;
  }
}

