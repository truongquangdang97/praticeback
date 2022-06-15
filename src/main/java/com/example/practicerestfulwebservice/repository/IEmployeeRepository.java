package com.example.practicerestfulwebservice.repository;


import com.example.practicerestfulwebservice.entity.Employee;

import java.util.List;

public interface IEmployeeRepository {
    List<Employee> getEmployees();
    Employee addEmployees(Employee product);
    Boolean updateEmployees(Integer id, Employee product);
    Employee findById(Integer id);
}
