package com.bakigoal.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@NamedQueries({
    @NamedQuery(name = "Employee.findAll",
        query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByPrimaryKey",
        query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByName",
        query = "SELECT e FROM Employee e WHERE e.lastName = :name")
})
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(name = "F_NAME")
  private String firstName;
  @Column(name = "L_NAME")
  private String lastName;
  @Embedded
  private EmployeeName name;
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

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "DEPT_ID")
  private Department department;

  /**
   * There are really only two cases in which cascading the remove() operation makes sense: one-to-one
   * and one-to-many relationships, in which there is a clear parent-child relationship
   */
  @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private ParkingSpace parkingSpace;

  @ManyToMany
  @JoinTable(name = "EMP_PROJ",
      joinColumns = @JoinColumn(name = "EMP_ID"),
      inverseJoinColumns = @JoinColumn(name = "PROJ_ID"))
  private List<Project> projects;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
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

  @ElementCollection
  @CollectionTable(name = "EMP_PHONE_MAP")
  @MapKeyEnumerated(EnumType.STRING)
  @MapKeyColumn(name = "PHONE_TYPE")
  @Column(name = "PHONE_NUM")
  private Map<PhoneType, String> phoneNumbers;

  // getters and setters ------------------

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public EmployeeName getName() {
    return name;
  }

  public void setName(EmployeeName name) {
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

  public Collection<VacationEntry> getVacations() {
    return vacations;
  }

  public void setVacations(Collection<VacationEntry> vacations) {
    this.vacations = vacations;
  }

  public Set<String> getNickNames() {
    return nickNames;
  }

  public void setNickNames(Set<String> nickNames) {
    this.nickNames = nickNames;
  }

  public Map<PhoneType, String> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(Map<PhoneType, String> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }
}

