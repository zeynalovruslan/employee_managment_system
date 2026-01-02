package com.employee.management.system.service.impl;

import com.employee.management.system.calculator.AttendanceCalculator;
import com.employee.management.system.calculator.SalaryCalculator;
import com.employee.management.system.dto.response.RespEmployeeInvoice;
import com.employee.management.system.entity.DayOffDay;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.EmployeeInvoice;
import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.mapper.EmployeeInvoiceMapper;
import com.employee.management.system.repository.DayOffDayRepository;
import com.employee.management.system.repository.EmployeeInvoiceRepository;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.RequestedVacationRepository;
import com.employee.management.system.service.EmployeeInvoiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service

@RequiredArgsConstructor
public class EmployeeInvoiceServiceImpl implements EmployeeInvoiceService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeInvoiceRepository employeeInvoiceRepository;
    private final RequestedVacationRepository requestedVacationRepository;
    private final EmployeeInvoiceMapper employeeInvoiceMapper;
    private final DayOffDayRepository dayOffDayRepository;
    private final SalaryCalculator salaryCalculator;
    private final AttendanceCalculator attendanceCalculator;

    @Transactional
    @Override
    public void calculateMonthlySalary(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        List<Employee> employeeList = employeeRepository.findEmployeeByStatus(EmployeeStatusEnum.CREATED).orElseThrow(()
                -> new EmployeeNotFoundException("Employee not found"));

        List<DayOffDay> holidays = dayOffDayRepository.findHolidayByYearAndMonth(year, month);

        for (Employee employee : employeeList) {

            boolean exists = employeeInvoiceRepository.existsByEmployeeIdAndYearAndMonth(employee.getId(), year, month);
            if (exists) {
                continue;
            }
            List<RequestedVacation> vacations = requestedVacationRepository.findRequestedVacationByEmployeeIdAndStatus(
                    employee.getId(),
                    RequestVacationStatusEnum.APPROVED);

            long workingDaysCount = attendanceCalculator.calculateWorkingDays(yearMonth, holidays, vacations, startOfMonth, endOfMonth);
            if (workingDaysCount <= 0) {
                throw new BadRequestException("Working days can not be zero");
            }

            BigDecimal baseSalary = employee.getSalary();

            BigDecimal dailySalary = salaryCalculator.calculateDailySalary(baseSalary, workingDaysCount);

            BigDecimal hourlySalary = salaryCalculator.calculateHourlySalary(dailySalary);

            BigDecimal minutelySalary = salaryCalculator.calculateMinutelySalary(hourlySalary);

            BigDecimal workingDaySalary = salaryCalculator.calculateWorkingDaySalary(dailySalary, workingDaysCount);

            long monthlyOverTime = attendanceCalculator.calculateMonthlyOverTime(employee.getId(), startOfMonth, endOfMonth);

            long monthlyLateTime = attendanceCalculator.calculateMonthlyLateTime(employee.getId(), startOfMonth, endOfMonth);

            long absentDayCount = attendanceCalculator.calculateAbsentDay(employee.getId(), startOfMonth, endOfMonth);

            long countWorkedOnHoliday = attendanceCalculator.calculateWorkedOnHoliday(employee.getId(), startOfMonth, endOfMonth);

            BigDecimal absentDayPenalty = salaryCalculator.calculateAbsentDaySalary(dailySalary, absentDayCount);

            BigDecimal monthlyOverTimeSalary = salaryCalculator.calculateOvertimeSalary(minutelySalary, monthlyOverTime);

            BigDecimal monthlyLateTimeSalary = salaryCalculator.calculateLateTimeSalary(minutelySalary, monthlyLateTime);

            BigDecimal dailyVacationSalary = salaryCalculator.calculateDailyVacationSalary(dailySalary);

            BigDecimal workedOnHolidaySalary = salaryCalculator.calculateWorkedOnDaySalary(dailySalary, countWorkedOnHoliday);

            BigDecimal vacationSalary = salaryCalculator.calculateVacationSalary(
                    vacations, holidays,
                    dailyVacationSalary
                    , startOfMonth, endOfMonth, year, month);

            BigDecimal totalSalary =
                    salaryCalculator.calculateTotalSalary(workingDaySalary, vacationSalary,
                            monthlyOverTimeSalary, monthlyLateTimeSalary, absentDayPenalty, workedOnHolidaySalary);

            attendanceCalculator.saveInvoice(employee, year, month, baseSalary,
                    vacationSalary, totalSalary, monthlyOverTime, monthlyLateTime,
                    monthlyOverTimeSalary, monthlyLateTimeSalary, absentDayCount,
                    absentDayPenalty, countWorkedOnHoliday, workedOnHolidaySalary);
        }
    }

    @Override
    public List<RespEmployeeInvoice> getInvoicesByEmployeeId(Long employeeId) {
        List<EmployeeInvoice> invoices = employeeInvoiceRepository.findByEmployeeId(employeeId).orElseThrow(()
                -> new NotFoundException("Invoices not found"));
        return invoices.stream().map(employeeInvoiceMapper::toResponse).toList();

    }
}
