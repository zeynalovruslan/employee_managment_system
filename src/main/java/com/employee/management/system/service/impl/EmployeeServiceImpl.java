package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqEmployee;
import com.employee.management.system.dto.response.RespEmployee;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.mapper.EmployeeMapper;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;


    @Override
    public List<RespEmployee> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findEmployeeByStatus(
                EmployeeStatusEnum.ACTIVE).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        return employeeList.stream().map(employeeMapper::toResponse).toList();
    }

    @Override
    public RespEmployee getEmployeeById(Long id) {
        Employee getEmployee = employeeRepository.findEmployeeByIdAndStatus
                (id, EmployeeStatusEnum.ACTIVE).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        return employeeMapper.toResponse(getEmployee);

    }

    @Override
    public RespEmployee createEmployee(ReqEmployee request) {
        Employee saveEmplooye = employeeRepository.save(employeeMapper.toEntity(request));
        return employeeMapper.toResponse(saveEmplooye);
    }

    @Override
    public RespEmployee updateEmployee(Long id, ReqEmployee request) {

        Employee employee = employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE).orElseThrow(
                () -> new EmployeeNotFoundException("Employee is not found")
        );

        employeeMapper.updateEmployeeFromRequest(request, employee);
        Employee saveEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponse(saveEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE).orElseThrow(
                () -> new EmployeeNotFoundException("Employee is not found "));

        employee.setStatus((EmployeeStatusEnum.TERMINATED));
        employeeRepository.save(employee);

    }


}
