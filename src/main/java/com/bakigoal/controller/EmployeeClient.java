package com.bakigoal.controller;

import com.bakigoal.model.Employee;
import com.bakigoal.repository.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/emp")
public class EmployeeClient {

  @Autowired
  private EmployeeDao employeeDao;

  @GetMapping
  public String employees(Model model) {
    List<Employee> allEmployees = employeeDao.findAllEmployees();
    model.addAttribute("emps", allEmployees);
    model.addAttribute("employee", new Employee());
    return "employees";
  }

  @GetMapping(value = "/image/{empId}")
  @ResponseBody
  public byte[] image(@PathVariable int empId) {
    Employee employee = employeeDao.findEmployee(empId);
    return employee == null ? new byte[0] : employee.getPicture();
  }

  @PostMapping
  public String addEmployee(@ModelAttribute Employee employee) {
    employeeDao.createEmployee(employee);
    return "redirect:/emp/";
  }

  @PostMapping("/image/{empId}")
  public String uploadPhoto(@RequestParam("file") MultipartFile file, @PathVariable int empId) {
    if (file.isEmpty()) {
      return "redirect:/emp/";
    }
    try {
      // Get the file and save it somewhere
      byte[] bytes = file.getBytes();
      Employee employee = employeeDao.findEmployee(empId);
      employee.setPicture(bytes);
      employeeDao.update(employee);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return "redirect:/emp/";
  }

  @GetMapping("delete")
  public String employee(@RequestParam int id) {
    employeeDao.removeEmployee(id);
    return "redirect:/emp/";
  }
}
