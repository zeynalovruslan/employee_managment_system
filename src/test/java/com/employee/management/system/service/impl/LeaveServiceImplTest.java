package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqLeave;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.Leave;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.LeaveStatusEnum;
import com.employee.management.system.enums.LeaveTypeEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.LeaveRepository;
import com.employee.management.system.repository.UserRepository;
import com.employee.management.system.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    LeaveRepository leaveRepository;

    @Mock
    Authentication authentication;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    LeaveServiceImpl leaveService;


    @Test
    void createHourlyLeave_shouldThrow_whenUserNotFound() {

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 10, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 25, 12, 0));

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, ()
                -> leaveService.createHourlyLeave(reqLeave, authentication));
        assertEquals("User not found", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(authentication).getName();
        verifyNoInteractions(leaveRepository);

    }

    @Test
    void createHourlyLeave_shouldThrow_whenEmployeeNotFound() {

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 10, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 25, 12, 0));

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, ()
                -> leaveService.createHourlyLeave(reqLeave, authentication));
        assertEquals("Employee not found", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verifyNoMoreInteractions(userRepository, employeeRepository, leaveRepository);
    }

    @Test
    void createHourlyLeave_fromOrToNull_throwsBadRequest() {

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(null);
        reqLeave.setEndAt(null);

        UserEntity user = new UserEntity();
        user.setId(1L);

        Employee employee = new Employee();
        employee.setId(1L);

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> leaveService.createHourlyLeave(reqLeave, authentication));
        assertEquals("from/to cannot be null", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verifyNoMoreInteractions(userRepository, employeeRepository, leaveRepository);
    }

    @Test
    void createHourlyLeave_fromAndToWithinDifferentDays_throwsBadRequest() {

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 10, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 26, 10, 0));

        UserEntity user = new UserEntity();
        user.setId(1L);

        Employee employee = new Employee();
        employee.setId(1L);

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));

        BadRequestException ex = assertThrows(BadRequestException.class, ()
                -> leaveService.createHourlyLeave(reqLeave, authentication));

        assertEquals("Hourly leave must be within the same day", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verifyNoMoreInteractions(userRepository, employeeRepository, leaveRepository);
    }

    @Test
    void creatHourlyLeave_HourlyLeaveOutSideWorkingHours_throwsBadRequest() {

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 8, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 25, 10, 0));

        UserEntity user = new UserEntity();
        user.setId(1L);

        Employee employee = new Employee();
        employee.setId(1L);

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));

        BadRequestException ex = assertThrows(BadRequestException.class, ()
                -> leaveService.createHourlyLeave(reqLeave, authentication));
        assertEquals("Hourly leave must be within 09:00 - 18:00", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verifyNoMoreInteractions(userRepository, employeeRepository, leaveRepository);
    }

    @Test
    void createHourlyLeave_StartAtIsBeforeEndAt_throwsBadRequest() {

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 11, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 25, 9, 0));

        UserEntity user = new UserEntity();
        user.setId(1L);

        Employee employee = new Employee();
        employee.setId(1L);

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));

        BadRequestException ex = assertThrows(BadRequestException.class, ()
                -> leaveService.createHourlyLeave(reqLeave, authentication));
        assertEquals("Invalid time range", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verifyNoMoreInteractions(userRepository, employeeRepository, leaveRepository);
    }

    @Test
    void createHourlyLeave_LeaveHoursMoreThanThreeHours_throwsBadRequest() {

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 10, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 25, 14, 0));

        UserEntity user = new UserEntity();
        user.setId(1L);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setLeaves(new ArrayList<>());


        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));

        BadRequestException ex = assertThrows(BadRequestException.class, ()
                -> leaveService.createHourlyLeave(reqLeave, authentication));
        assertEquals("It can't be more than 3 hours.", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verifyNoMoreInteractions(userRepository, employeeRepository, leaveRepository);
    }

    @Test
    void createHourlyLeave_OverLapHourlyLeave_throwsBadRequest() {

        Leave leave = new Leave();
        leave.setStartAt(LocalDateTime.of(2026, 1, 25, 11, 0));
        leave.setEndAt(LocalDateTime.of(2026, 1, 25, 13, 0));
        leave.setLeaveType(LeaveTypeEnum.HOURLY);
        leave.setRequestStatus(LeaveStatusEnum.PENDING);

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 10, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 25, 12, 0));

        UserEntity user = new UserEntity();
        user.setId(1L);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setLeaves(List.of(leave));

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));

        BadRequestException ex = assertThrows(BadRequestException.class, ()
                -> leaveService.createHourlyLeave(reqLeave, authentication));
        assertEquals("Hourly leave already exists", ex.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verifyNoMoreInteractions(userRepository, employeeRepository, leaveRepository);
    }

    @Test
    void createHourlyLeave_success_savesLeaves() {

        when(authentication.getName()).thenReturn("testUser");

        UserEntity user = new UserEntity();
        user.setId(1L);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setLeaves(new ArrayList<>());

        ReqLeave reqLeave = new ReqLeave();
        reqLeave.setStartAt(LocalDateTime.of(2026, 1, 25, 9, 0));
        reqLeave.setEndAt(LocalDateTime.of(2026, 1, 25, 11, 0));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(employeeRepository.findByUser_IdAndStatus(user.getId(),
                EmployeeStatusEnum.ACTIVE)).thenReturn(Optional.of(employee));

        leaveService.createHourlyLeave(reqLeave, authentication);

        verify(userRepository).findByUsername("testUser");
        verify(employeeRepository).findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE);
        verify(authentication).getName();
        verify(leaveRepository).save(any(Leave.class));
    }

}