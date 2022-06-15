package com.example.practicerestfulwebservice.resource;


import com.example.practicerestfulwebservice.entity.Employee;
import com.example.practicerestfulwebservice.repository.EmployeeRepository;

import javax.ws.rs.*;
import java.util.List;

@Path("/employees")
@Produces("application/json")
@Consumes("application/json")
public class EmployeeResource {
    private final EmployeeRepository employeeRepository;

    public EmployeeResource() {
        employeeRepository = new EmployeeRepository();
    }

    @GET
    public List<Employee> getEmployees(){
        return employeeRepository.getEmployees();
    }

    @GET
    @Path("{id}")
    public Employee findById(@PathParam("id") Integer id){
        return employeeRepository.findById(id);
    }

    @POST
    public Employee addEmployees(Employee employee){
        return employeeRepository.addEmployees(employee);
    }

    @PUT
    @Path("{id}")
    public Boolean updateEmployees(@PathParam("id") Integer id, Employee employee){
        return employeeRepository.updateEmployees(id, employee);
    }
}
