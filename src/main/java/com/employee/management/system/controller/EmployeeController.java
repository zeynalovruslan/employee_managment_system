package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqEmployee;
import com.employee.management.system.dto.response.RespEmployee;
import com.employee.management.system.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/employees")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public List<RespEmployee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public RespEmployee getEmployeeById(@PathVariable @NotNull(message = "Employee id is cannot empty") Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public RespEmployee addEmployee(@RequestBody @Valid ReqEmployee request) {
        return employeeService.createEmployee(request);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public RespEmployee updateEmployee(@PathVariable @NotNull Long id,
                                       @RequestBody @Valid ReqEmployee request) {
        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public void terminateEmployeeById(@PathVariable @NotNull Long employeeId) {
        employeeService.terminateEmployeeById(employeeId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','DEPARTMENT_DIRECTOR')")
    @GetMapping("/{departmentId}/department-employees")
    public List<RespEmployee> getEmployeeListByDepartmentId(@PathVariable Long departmentId) {
        return employeeService.getEmployeeListByDepartmentId(departmentId);
    }
}

