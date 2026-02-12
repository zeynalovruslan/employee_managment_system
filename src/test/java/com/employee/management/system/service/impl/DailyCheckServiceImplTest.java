package com.employee.management.system.service.impl;


import com.employee.management.system.dto.request.ReqDailyCheck;
import com.employee.management.system.entity.DailyCheck;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.repository.DailyCheckRepository;
import com.employee.management.system.repository.DayOffDayRepository;
import com.employee.management.system.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyCheckServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    DayOffDayRepository dayOffDayRepository;
    @Mock
    DailyCheckRepository dailyCheckRepository;
    @InjectMocks
    DailyCheckServiceImpl dailyCheckService;


    @Test
    void addInputAndOutput_shouldThrow_whenEmployeeNotFound() {
        ReqDailyCheck reqDailyCheck = new ReqDailyCheck();
        reqDailyCheck.setEmployeeId(1L);

        Long employeeId = reqDailyCheck.getEmployeeId();

        when(employeeRepository.findEmployeeById(employeeId)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> {
                    dailyCheckService.addInputAndOutput(reqDailyCheck);
                });
        assertEquals("Employee is not found", ex.getMessage());

        verify(employeeRepository).findEmployeeById(employeeId);
        verifyNoInteractions(dailyCheckRepository);
    }

    @Test
    void addInputAndOutput_shouldSave_whenHolidaysAndWeekday() {

        ReflectionTestUtils.setField(dailyCheckService, "startWorkTime", LocalTime.of(9, 0));
        ReflectionTestUtils.setField(dailyCheckService, "endWorkTime", LocalTime.of(18, 0));

        ReqDailyCheck reqDailyCheck = new ReqDailyCheck();
        reqDailyCheck.setEmployeeId(1L);
        reqDailyCheck.setEntryTime(LocalTime.of(9, 0));
        reqDailyCheck.setExitTime(LocalTime.of(18, 0));

        Long employeeId = reqDailyCheck.getEmployeeId();

        Employee employee = new Employee();
        employee.setId(employeeId);

        LocalDate now = LocalDate.now();

        int year = now.getYear();
        int month = now.getMonthValue();

        when(employeeRepository.findEmployeeById(employeeId)).thenReturn(Optional.of(employee));
        when(dayOffDayRepository.findHolidayByYearAndMonth(year, month)).thenReturn(List.of());

        dailyCheckService.addInputAndOutput(reqDailyCheck);

        verify(employeeRepository).findEmployeeById(employeeId);
        verify(dayOffDayRepository).findHolidayByYearAndMonth(year, month);
    }



    @Test
    void addInputAndOutput_shouldSaveDailyCheck_whenRequestIsValid() {

        ReflectionTestUtils.setField(dailyCheckService, "startWorkTime", LocalTime.of(9, 0));
        ReflectionTestUtils.setField(dailyCheckService, "endWorkTime", LocalTime.of(18, 0));

        ReqDailyCheck reqDailyCheck = new ReqDailyCheck();
        reqDailyCheck.setEmployeeId(1L);
        reqDailyCheck.setEntryTime(LocalTime.of(9, 0));
        reqDailyCheck.setExitTime(LocalTime.of(18, 0));

        Long employeeId = reqDailyCheck.getEmployeeId();

        Employee employee = new Employee();
        employee.setId(employeeId);

        LocalDate now = LocalDate.now();

        int year = now.getYear();
        int month = now.getMonthValue();

        when(employeeRepository.findEmployeeById(employeeId)).thenReturn(Optional.of(employee));
        when(dayOffDayRepository.findHolidayByYearAndMonth(year, month)).thenReturn(List.of());

        dailyCheckService.addInputAndOutput(reqDailyCheck);

        verify(dailyCheckRepository).save(any(DailyCheck.class));

    }




}