package com.bakigoal.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EmployeeName {
  @Column(name = "F_NAME", insertable = false, updatable = false)
  private String first;
  @Column(name = "L_NAME", insertable = false, updatable = false)
  private String last;

  public String getFirst() {
    return first;
  }

  public void setFirst(String firstName) {
    this.first = firstName;
  }

  public String getLast() {
    return last;
  }

  public void setLast(String lastName) {
    this.last = lastName;
  }
}
