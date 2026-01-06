package com.employee.management.system.controller;

import com.employee.management.system.dto.response.RespEmployee;
import com.employee.management.system.dto.request.ReqEmployee;
import com.employee.management.system.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping
    public List<RespEmployee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public RespEmployee getEmployeeById(@PathVariable @NotNull(message = "Employee id is cannot empty") Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RespEmployee addEmployee(@RequestBody @Valid ReqEmployee request) {
        return employeeService.createEmployee(request);

    }

    @PutMapping("/{id}")
    public RespEmployee updateEmployee(@PathVariable @NotNull Long id,
                                       @RequestBody @Valid ReqEmployee request) {
        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable @NotNull Long id) {
        employeeService.deleteEmployee(id);
    }
}

