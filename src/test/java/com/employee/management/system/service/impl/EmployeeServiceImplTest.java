package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqEmployee;
import com.employee.management.system.dto.response.RespEmployee;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.mapper.EmployeeMapper;
import com.employee.management.system.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;


    @Test
    void getAllEmployees_shouldReturnList_whenActiveEmployeesExists() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Name");

        List<Employee> employeeList = List.of(employee);

        when(employeeRepository.findEmployeeByStatus(EmployeeStatusEnum.ACTIVE)).thenReturn(employeeList);

        RespEmployee resp = new RespEmployee();
        resp.setId(1L);
        resp.setName("Name");
        when(employeeMapper.toResponse(employee)).thenReturn(resp);

        List<RespEmployee> result = employeeServiceImpl.getAllEmployees();
        assertEquals(1, result.size());
        assertEquals("Name", result.get(0).getName());

        verify(employeeRepository).findEmployeeByStatus(EmployeeStatusEnum.ACTIVE);
        verify(employeeMapper).toResponse(employee);
    }


    @Test
    void getAllEmployees_shouldReturnList_whenActiveEmployeesIsEmpty() {

        when(employeeRepository.findEmployeeByStatus(EmployeeStatusEnum.ACTIVE)).thenReturn(List.of());

        List<RespEmployee> result = employeeServiceImpl.getAllEmployees();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository).findEmployeeByStatus(EmployeeStatusEnum.ACTIVE);
        verifyNoInteractions(employeeMapper);
    }


    @Test
    void getEmployeeById_shouldReturnResp_whenEmployeeExistsAndActive() {
        Long id = 1L;

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Name");

        RespEmployee resp = new RespEmployee();
        resp.setId(id);
        resp.setName("Name");

        when(employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(resp);

        RespEmployee response = employeeServiceImpl.getEmployeeById(id);

        assertNotNull(response);
        assertEquals("Name", response.getName());
        assertEquals(id, response.getId());

        verify(employeeRepository).findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE);
        verify(employeeMapper).toResponse(employee);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);

    }

    @Test
    void getEmployeeById_shouldThrow_whenEmployeeNotFound() {
        Long id = 1L;
        when(employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.empty());
        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.getEmployeeById(id));
        assertEquals("Employee not found", ex.getMessage());

        verify(employeeRepository).findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void createEmployee_shouldSaveAndReturnResponse() {
        ReqEmployee reqEmployee = new ReqEmployee();
        reqEmployee.setName("Name");

        Employee employeeToSave = new Employee();
        employeeToSave.setName("Name");

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setName("Name");

        RespEmployee respEmployee = new RespEmployee();
        respEmployee.setId(1L);
        respEmployee.setName("Name");

        when(employeeMapper.toEntity(reqEmployee)).thenReturn(employeeToSave);
        when(employeeRepository.save(employeeToSave)).thenReturn(savedEmployee);
        when(employeeMapper.toResponse(savedEmployee)).thenReturn(respEmployee);

        RespEmployee result = employeeServiceImpl.createEmployee(reqEmployee);

        assertNotNull(result);
        assertEquals("Name", result.getName());
        assertEquals(1L, result.getId());

        verify(employeeMapper).toEntity(reqEmployee);
        verify(employeeRepository).save(employeeToSave);
        verify(employeeMapper).toResponse(savedEmployee);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void updateEmployee_shouldUpdateAndReturnResponse_whenEmployeeExists() {
        Long id = 1L;
        ReqEmployee reqEmployee = new ReqEmployee();
        reqEmployee.setName("Update Name");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(id);
        existingEmployee.setName("Old Name");

        Employee savedEmployee = new Employee();
        savedEmployee.setId(id);
        savedEmployee.setName("Update Name");

        RespEmployee respEmployee = new RespEmployee();
        respEmployee.setId(id);
        respEmployee.setName("Update Name");

        when(employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(savedEmployee);
        when(employeeMapper.toResponse(savedEmployee)).thenReturn(respEmployee);

        RespEmployee result = employeeServiceImpl.updateEmployee(id, reqEmployee);

        assertNotNull(result);
        assertEquals("Update Name", result.getName());
        assertEquals(id, result.getId());

        verify(employeeRepository).findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE);
        verify(employeeMapper).updateEmployeeFromRequest(reqEmployee, existingEmployee);
        verify(employeeRepository).save(existingEmployee);

        verify(employeeMapper).toResponse(savedEmployee);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);

    }

    @Test
    void updateEmployee_shouldThrow_whenEmployeeNotFound() {
        Long id = 1L;
        ReqEmployee reqEmployee = new ReqEmployee();
        reqEmployee.setName("Name");

        when(employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.empty());
        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> employeeServiceImpl.updateEmployee(id, reqEmployee));

        assertEquals("Employee is not found", ex.getMessage());
        verify(employeeRepository).findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE);
        verifyNoMoreInteractions(employeeMapper, employeeRepository);
    }

    @Test
    void deleteEmployee_shouldSetTerminatedAndTerminatedDate_andSave_whenEmployeeExists() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(EmployeeStatusEnum.ACTIVE);

        when(employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));
        employeeServiceImpl.terminateEmployeeById(id);

        assertEquals(EmployeeStatusEnum.TERMINATED, employee.getStatus());
        assertEquals(LocalDate.now(), employee.getTerminateDate());

        verify(employeeRepository).findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE);
        verify(employeeRepository).save(employee);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void deleteEmployee_shouldThrow_whenEmployeeNotFound() {
        Long id = 1L;

        when(employeeRepository.findEmployeeByIdAndStatus(id, EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.terminateEmployeeById(id));

        verify(employeeRepository, never()).save(any());

    }

    @Test
    void getEmployeeListByDepartmentId_shouldReturnResponse() {
        Long departmentId = 1L;

        Employee employee = new Employee();

        RespEmployee respEmployee = new RespEmployee();

        when(employeeRepository.findByPosition_Department_IdAndStatus(departmentId, EmployeeStatusEnum.ACTIVE)).
                thenReturn(Optional.of(List.of(employee)));

        when(employeeMapper.toResponse(employee)).thenReturn(respEmployee);

        List<RespEmployee> result = employeeServiceImpl.getEmployeeListByDepartmentId(departmentId);

        assertEquals(1, result.size());
        assertEquals(respEmployee, result.get(0));

        verify(employeeRepository)
                .findByPosition_Department_IdAndStatus(departmentId, EmployeeStatusEnum.ACTIVE);

        verify(employeeMapper).toResponse(employee);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);

    }

    @Test
    void getEmployeeListByDepartmentId_shouldThrow_whenEmployeeNotFound() {
        Long departmentId = 1L;

        when(employeeRepository.findByPosition_Department_IdAndStatus(departmentId, EmployeeStatusEnum.ACTIVE)).
                thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> employeeServiceImpl.getEmployeeListByDepartmentId(departmentId));

        assertEquals("Employee not found", ex.getMessage());

        verify(employeeRepository).findByPosition_Department_IdAndStatus(departmentId, EmployeeStatusEnum.ACTIVE);
        verifyNoInteractions(employeeMapper);

    }
}