package com.employee.management.system.calculator;

import com.employee.management.system.entity.*;
import com.employee.management.system.enums.CheckStatusEnum;
import com.employee.management.system.repository.DailyCheckRepository;
import com.employee.management.system.repository.EmployeeInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AttendanceCalculator {

    private final DailyCheckRepository dailyCheckRepository;

    private final EmployeeInvoiceRepository employeeInvoiceRepository;

    public long calculateMonthlyOverTime(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<DailyCheck> overTime = dailyCheckRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate);
        long totalOverTime = overTime.stream().mapToLong(DailyCheck::getOverTime).sum();
        return totalOverTime;
    }


    public long calculateMonthlyLateTime(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<DailyCheck> lateTime = dailyCheckRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate);
        long totalLateTime = lateTime.stream().mapToLong(DailyCheck::getLateTime).sum();
        return totalLateTime;
    }


    public long calculateAbsentDay(Long employeeId, LocalDate startDate, LocalDate endDate) {
        long countAbsentDay = dailyCheckRepository.countByEmployeeIdAndStatusAndWorkDateBetween(
                employeeId, CheckStatusEnum.ABSENT, startDate, endDate);
        return countAbsentDay;
    }

    public long calculateWorkedOnHoliday(Long employeeId, LocalDate startDate, LocalDate endDate) {
        long workedOnHoliday = dailyCheckRepository.countByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate);
        return workedOnHoliday;
    }


    public long calculateWorkingDays(YearMonth yearMonth,
                                     List<DayOffDay> dayOffDays,
                                     List<RequestedVacation> requestedVacations,
                                     LocalDate startOfMonth, LocalDate endOfMonth) {

        Set<LocalDate> holidaySet = dayOffDays.stream().map(dayOffDay
                -> LocalDate.of(dayOffDay.getYear(), dayOffDay.getMonth(), dayOffDay.getHoliday())).collect(Collectors.toSet());

        Set<LocalDate> vacationSet = requestedVacations.stream().flatMap(v -> {
            LocalDate start = v.getStartDay().isBefore(startOfMonth) ? startOfMonth : v.getStartDay();
            LocalDate end = v.getEndDay().isAfter(endOfMonth) ? endOfMonth : v.getEndDay();
            if (start.isAfter(end)) {
                return Stream.empty();
            }

            return start.datesUntil(end.plusDays(1));
        }).collect(Collectors.toSet());

        long workingDays = 0;
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {

            LocalDate yearMonthDay = yearMonth.atDay(i);
            DayOfWeek dayOfWeek = yearMonthDay.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                continue;
            }
            if (holidaySet.contains(yearMonthDay)) {
                continue;
            }
            if (vacationSet.contains(yearMonthDay)) {
                continue;
            }
            workingDays++;
        }
        return workingDays;
    }


    public void saveInvoice(Employee employee, int year, int month, BigDecimal baseSalary,
                            BigDecimal vacationSalary, BigDecimal totalSalary, long monthlyOverTime,
                            long monthlyLateTime, BigDecimal monthlyOverTimeSalary, BigDecimal monthlyLateTimeSalary,
                            long absentDayCount, BigDecimal absentDayPenalty, long countWorkedHoliday, BigDecimal workedHolidaySalary) {

        EmployeeInvoice employeeInvoice = new EmployeeInvoice();
        employeeInvoice.setEmployee(employee);
        employeeInvoice.setYear(year);
        employeeInvoice.setMonth(month);
        employeeInvoice.setBaseSalary(baseSalary);
        employeeInvoice.setVacationSalary(vacationSalary);
        employeeInvoice.setTotalSalary(totalSalary);
        employeeInvoice.setCreatedAt(LocalDateTime.now());
        employeeInvoice.setOverTime(monthlyOverTime);
        employeeInvoice.setLateTime(monthlyLateTime);
        employeeInvoice.setOverTimeSalary(monthlyOverTimeSalary);
        employeeInvoice.setLateTimeSalary(monthlyLateTimeSalary);
        employeeInvoice.setAbsentDayCount(absentDayCount);
        employeeInvoice.setAbsentDayPenalty(absentDayPenalty);
        employeeInvoice.setCountWorkedHoliday(countWorkedHoliday);
        employeeInvoice.setWorkedHolidaySalary(workedHolidaySalary);

        employeeInvoiceRepository.save(employeeInvoice);
    }


}
