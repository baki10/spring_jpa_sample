package com.bakigoal.controller;

import com.bakigoal.model.Employee;
import com.bakigoal.service.EmployeeService;
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
  private EmployeeService employeeService;

  @GetMapping
  public String employees(Model model) {
    List<Employee> allEmployees = employeeService.findAllEmployees();
    model.addAttribute("emps", allEmployees);
    model.addAttribute("employee", new Employee());
    return "employees";
  }

  @GetMapping(value = "/image/{empId}")
  @ResponseBody
  public byte[] image(@PathVariable long empId) {
    Employee employee = employeeService.findEmployee(empId);
    return employee == null ? new byte[0] : employee.getPicture();
  }

  @PostMapping
  public String addEmployee(@ModelAttribute Employee employee) {
    employeeService.createEmployee(employee);
    return "redirect:/emp/";
  }

  @PostMapping("/image/{empId}")
  public String uploadPhoto(@RequestParam("file") MultipartFile file, @PathVariable long empId) {
    if (file.isEmpty()) {
      return "redirect:/emp/";
    }
    try {
      // Get the file and save it somewhere
      byte[] bytes = file.getBytes();
      Employee employee = employeeService.findEmployee(empId);
      employee.setPicture(bytes);
      employeeService.update(employee);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return "redirect:/emp/";
  }

  @GetMapping("delete")
  public String employee(@RequestParam long id) {
    employeeService.removeEmployee(id);
    return "redirect:/emp/";
  }
}
