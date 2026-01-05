package com.employee.management.system.calculator;

import com.employee.management.system.entity.DayOffDay;
import com.employee.management.system.entity.EmployeeInvoice;
import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.repository.DailyCheckRepository;
import com.employee.management.system.repository.DayOffDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SalaryCalculator {

    private final double VACATION_PERCENT = 0.75;
    private final long DAILY_WORK_HOURS = 8;
    private final long MINUTES = 60;
    private final DayOffDayRepository dayOffDayRepository;
    private final DailyCheckRepository dailyCheckRepository;

    public BigDecimal calculateWorkingDaySalary(BigDecimal dailySalary, long workingDay) {
        return dailySalary.multiply(new BigDecimal(workingDay));
    }

    public BigDecimal calculateDailySalary(BigDecimal baseSalary, long workingDay) {

        return baseSalary.divide(new BigDecimal(workingDay), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateHourlySalary(BigDecimal dailySalary) {
        return dailySalary.divide(BigDecimal.valueOf(DAILY_WORK_HOURS), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMinutelySalary(BigDecimal hourlySalary) {
        return hourlySalary.divide(BigDecimal.valueOf(MINUTES), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateDailyVacationSalary(BigDecimal dailySalary) {
        return dailySalary.multiply(BigDecimal.valueOf(VACATION_PERCENT));
    }

    public BigDecimal calculateOvertimeSalary(BigDecimal minutelySalary, long monthlyOverTime) {
        return minutelySalary.multiply(new BigDecimal(monthlyOverTime));
    }

    public BigDecimal calculateLateTimeSalary(BigDecimal minutelySalary, long monthlyLateTime) {
        return minutelySalary.multiply(new BigDecimal(monthlyLateTime));
    }

    public BigDecimal calculateAbsentDaySalary(BigDecimal dailySalary, long absentDayCount) {
        return dailySalary.multiply(new BigDecimal(absentDayCount));
    }


    public BigDecimal calculateWorkedOnDaySalary(BigDecimal dailySalary, long workedOnDayCount) {
        return dailySalary.multiply(new BigDecimal(workedOnDayCount).multiply(BigDecimal.valueOf(2)));
    }

    public BigDecimal calculateVacationSalary(List<RequestedVacation> vacations, List<DayOffDay> holidays,
                                              BigDecimal dailyVacationSalary, LocalDate startOfMonth, LocalDate endOfMonth,
                                              int year, int month) {

        BigDecimal vacationSalary = vacations.stream().map(v -> {
            LocalDate vacationStart = v.getStartDay().isBefore(startOfMonth) ? startOfMonth : v.getStartDay();
            LocalDate vacationEnd = v.getEndDay().isAfter(endOfMonth) ? endOfMonth : v.getEndDay();
            if (vacationStart.isAfter(vacationEnd)) {
                return BigDecimal.ZERO;
            }

            Set<LocalDate> holidaySet = holidays.stream()
                    .map(h -> LocalDate.of(h.getYear(), h.getMonth(), h.getHoliday()))
                    .collect(Collectors.toSet());
            Set<LocalDate> vacationDaysSet = vacationStart.datesUntil(vacationEnd.plusDays(1))
                    .filter(d -> {
                        DayOfWeek dow = d.getDayOfWeek();
                        return dow != DayOfWeek.SATURDAY &&
                                dow != DayOfWeek.SUNDAY &&
                                !holidaySet.contains(d);
                    })
                    .collect(Collectors.toSet());
            return dailyVacationSalary.multiply(BigDecimal.valueOf(vacationDaysSet.size()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        return vacationSalary;
    }

    public BigDecimal calculateTotalSalary(BigDecimal workingDaySalary, BigDecimal vacationSalary,
                                           BigDecimal monthlyOverTimeSalary, BigDecimal monthlyLateTimeSalary,
                                           BigDecimal absentDaySalary ,BigDecimal workedHolidaySalary) {
        return workingDaySalary
                .add(vacationSalary)
                .add(workedHolidaySalary)
                .add(monthlyOverTimeSalary)
                .subtract(monthlyLateTimeSalary).add(absentDaySalary);

    }

}
