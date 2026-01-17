package com.employee.management.system.service;

import com.employee.management.system.dto.request.ReqEmployeeCreate;
import com.employee.management.system.dto.response.RespEmployee;

import java.util.List;

public interface EmployeeService {

    List<RespEmployee> getAllEmployees();

    RespEmployee getEmployeeById(Long id);

    RespEmployee createEmployee(ReqEmployeeCreate request);

    RespEmployee updateEmployee(Long id, ReqEmployeeCreate request);

    void deleteEmployee(Long id);

     List<RespEmployee> getEmployeeListByDepartmentId(Long departmentId);

}
