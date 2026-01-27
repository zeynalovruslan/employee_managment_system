package com.employee.management.system.service;

import com.employee.management.system.dto.request.ReqEmployee;
import com.employee.management.system.dto.response.RespEmployee;

import java.util.List;

public interface EmployeeService {

    List<RespEmployee> getAllEmployees();

    RespEmployee getEmployeeById(Long id);

    RespEmployee createEmployee(ReqEmployee request);

    RespEmployee updateEmployee(Long id, ReqEmployee request);

    void terminateEmployeeById(Long employeeId);

     List<RespEmployee> getEmployeeListByDepartmentId(Long departmentId);

}
