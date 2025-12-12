package com.example.Login.service;

import com.example.Login.model.Employee;
import com.example.Login.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // CREATE
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // READ ALL
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // READ BY ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));
    }

    // UPDATE
    public Employee updateEmployee(Long id, Employee employeeDetails) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        existing.setFullName(employeeDetails.getFullName());
        existing.setEmail(employeeDetails.getEmail());
        existing.setDepartment(employeeDetails.getDepartment());
        existing.setSalary(employeeDetails.getSalary());

        return employeeRepository.save(existing);
    }

    // DELETE
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
