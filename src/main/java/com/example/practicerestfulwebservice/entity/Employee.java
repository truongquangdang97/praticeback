package com.example.practicerestfulwebservice.entity;

import com.example.practicerestfulwebservice.annotation.Column;
import com.example.practicerestfulwebservice.annotation.Id;
import com.example.practicerestfulwebservice.annotation.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "employees")
public class Employee {
    @Id(autoIncrement = true)
    @Column(name = "id", type = "INT")
    private Integer id;
    @Column(name = "name", type = "VARCHAR(255)")
    private String name;
    @Column(name = "salary", type = "DECIMAL")
    private BigDecimal salary;

    public Employee(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
    }
}
