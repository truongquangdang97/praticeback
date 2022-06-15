package com.example.practicerestfulwebservice.repository;

import com.example.practicerestfulwebservice.entity.Employee;
import com.example.practicerestfulwebservice.model.GenericModel;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeRepository implements IEmployeeRepository{
    private final GenericModel<Employee> employeeModel = new GenericModel<>(Employee.class);


    @Override
    public List<Employee> getEmployees() {
        if (employeeModel.getAll().size() <= 0){
            employeeModel.save(new Employee("Hoang Tu", new BigDecimal(1000)));
            employeeModel.save(new Employee("Bui Chi Thanh", new BigDecimal(1500)));
            employeeModel.save(new Employee("Diep Minh Tuan", new BigDecimal(2000)));
            employeeModel.save(new Employee("Doan Minh Duc", new BigDecimal(1200)));
            employeeModel.save(new Employee("Truong Dang Quang", new BigDecimal(3500)));
        }

        return employeeModel.getAll();
    }

    @Override
    public Employee addEmployees(Employee employee) {
        employeeModel.save(employee);
        return employee;
    }

    @Override
    public Boolean updateEmployees(Integer id, Employee updateEmployee) {
        Employee existProduct = employeeModel.findById(id);

        existProduct.setName(updateEmployee.getName());
        existProduct.setSalary(updateEmployee.getSalary());

        return employeeModel.update(id, existProduct);
    }

    @Override
    public Employee findById(Integer id) {
        return employeeModel.findById(id);
    }
}
