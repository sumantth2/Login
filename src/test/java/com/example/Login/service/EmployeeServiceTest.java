package com.example.Login.service;

import com.example.Login.model.Employee;
import com.example.Login.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee emp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        emp = new Employee();
        emp.setId(1L);
        emp.setFullName("John Doe");
        emp.setEmail("john@gmail.com");
        emp.setDepartment("IT");
        emp.setSalary(5000.0);
    }

    // CREATE
    @Test
    void testSaveEmployee() {
        when(employeeRepository.save(emp)).thenReturn(emp);

        Employee saved = employeeService.saveEmployee(emp);

        assertNotNull(saved);
        assertEquals("John Doe", saved.getFullName());
        verify(employeeRepository, times(1)).save(emp);
    }

    // READ ALL
    @Test
    void testGetAllEmployees() {
        List<Employee> list = Arrays.asList(emp);

        when(employeeRepository.findAll()).thenReturn(list);

        List<Employee> result = employeeService.getAllEmployees();
        System.out.println("Employees fetched: " + result.toString());
        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    // READ BY ID
    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = employeeService.getEmployeeById(1L);
        System.out.println("Fetched Employee: " + result);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class,
                () -> employeeService.getEmployeeById(1L));

        assertEquals("Employee Not Found", ex.getMessage());
    }

    // UPDATE
    @Test
    void testUpdateEmployee() {
        Employee updated = new Employee();
        updated.setFullName("Updated Name");
        updated.setEmail("update@gmail.com");
        updated.setDepartment("HR");
        updated.setSalary(8000.0);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(employeeRepository.save(emp)).thenReturn(emp);

        Employee result = employeeService.updateEmployee(1L, updated);

        assertEquals("Updated Name", result.getFullName());
        assertEquals("update@gmail.com", result.getEmail());
        verify(employeeRepository, times(1)).save(emp);
    }

    // DELETE
    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
