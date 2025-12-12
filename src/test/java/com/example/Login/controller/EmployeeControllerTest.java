package com.example.Login.controller;

import com.example.Login.model.Employee;
import com.example.Login.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

// NEW import (replacement for @MockBean)
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ✅ Replacement for @MockBean
    @MockitoBean
    private EmployeeService employeeService;

    // ------------------------------
    // TEST: POST /api/employees
    // ------------------------------
    @Test
    void testAddEmployee() throws Exception {

        Employee employee = new Employee("John Doe", "john@example.com", "IT", 70000);
        employee.setId(1L);

        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "John Doe",
                                  "email": "john@example.com",
                                  "department": "IT",
                                  "salary": 70000
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.id").value(1));
    }

    // ------------------------------
    // TEST: GET /api/employees
    // ------------------------------
    @Test
    void testGetAllEmployees() throws Exception {

        List<Employee> employees = Arrays.asList(
                new Employee("John", "john@example.com", "IT", 70000),
                new Employee("Sam", "sam@example.com", "HR", 60000)
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(2));
    }

    // ------------------------------
    // TEST: GET /api/employees/{id}
    // ------------------------------
    @Test
    void testGetEmployeeById() throws Exception {

        Employee employee = new Employee("John Doe", "john@example.com", "IT", 70000);
        employee.setId(1L);

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    // ------------------------------
    // TEST: PUT /api/employees/{id}
    // ------------------------------
    @Test
    void testUpdateEmployee() throws Exception {

        Employee updated = new Employee("Updated", "updated@example.com", "Finance", 90000);
        updated.setId(1L);

        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Updated",
                                  "email": "updated@example.com",
                                  "department": "Finance",
                                  "salary": 90000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated"));
    }

    // ------------------------------
    // TEST: DELETE /api/employees/{id}
    // ------------------------------
    @Test
    void testDeleteEmployee() throws Exception {

        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));
    }
}
