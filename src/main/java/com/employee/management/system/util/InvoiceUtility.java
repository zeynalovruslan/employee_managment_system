package com.employee.management.system.util;

import com.employee.management.system.entity.DayOffDay;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.EmployeeInvoice;
import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.repository.DayOffDayRepository;
import com.employee.management.system.repository.EmployeeInvoiceRepository;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.RequestedVacationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Component
@Service
public class InvoiceUtility {

    private final EmployeeRepository employeeRepository;
    private final EmployeeInvoiceRepository employeeInvoiceRepository;
    private final DayOffDayRepository dayOffDayRepository;
    private final RequestedVacationRepository requestedVacationRepository;




    private List<Employee> getEmployeesByStatus(EmployeeStatusEnum status) {
        List<Employee> employees = employeeRepository.findEmployeeByStatus(status).orElseThrow(()
                -> new EmployeeNotFoundException("Employee not found"));
        return employees;
    }

    private boolean invoiceExists(Long employeeId, int year, int month) {
        return employeeInvoiceRepository.existsByEmployeeIdAndYearAndMonth(employeeId, year, month);
    }


    private int getHolidayCount(int year, int month) {
        return dayOffDayRepository.findHolidayByYearAndMonth(year, month).size();
    }

    private int getWeekendCount(YearMonth yearMonth) {
        int weekendCount = 0;
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            DayOfWeek dayOfWeek = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), i).getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendCount++;
            }
        }
        return weekendCount;
    }

    private BigDecimal calculateWorkingDays(YearMonth yearMonth, int weekendCount, int holidayCount) {
        int totalWorkingDays = yearMonth.lengthOfMonth() - weekendCount - holidayCount;
        if (totalWorkingDays <= 0) {
            throw new IllegalStateException("No working days in the month");
        }
        return BigDecimal.valueOf(totalWorkingDays);
    }

    private BigDecimal calculateDailySalary(BigDecimal baseSalary, BigDecimal workingDays) {
        return baseSalary.divide(workingDays, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateVacationSalary(Long employeeId, BigDecimal dailyVacationSalary, LocalDate startOfMonth, LocalDate endOfMonth) {
        return requestedVacationRepository.findRequestedVacationByEmployeeIdAndStatus(employeeId, RequestVacationStatusEnum.APPROVED)
                .stream()
                .map(requestedVacation -> {
                    LocalDate vacationStart = requestedVacation.getStartDay().isBefore(startOfMonth) ? startOfMonth : requestedVacation.getStartDay();
                    LocalDate vacationEnd = requestedVacation.getEndDay().isAfter(endOfMonth) ? endOfMonth : requestedVacation.getEndDay();
                    if (vacationStart.isAfter(vacationEnd)) return BigDecimal.ZERO;
                    long vacationDays = ChronoUnit.DAYS.between(vacationStart, vacationEnd) + 1;
                    return dailyVacationSalary.multiply(BigDecimal.valueOf(vacationDays));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private EmployeeInvoice createEmployeeInvoice(Employee employee, int year, int month, BigDecimal baseSalary, BigDecimal vacationSalary) {
        EmployeeInvoice invoice = new EmployeeInvoice();
        invoice.setEmployee(employee);
        invoice.setYear(year);
        invoice.setMonth(month);
        invoice.setBaseSalary(baseSalary);
        invoice.setVacationSalary(vacationSalary);
        invoice.setTotalSalary(baseSalary.add(vacationSalary));
        invoice.setCreatedAt(LocalDateTime.now());
        return invoice;
    }
}






