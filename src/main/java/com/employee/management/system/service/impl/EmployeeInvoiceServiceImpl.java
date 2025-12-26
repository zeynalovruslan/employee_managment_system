package com.employee.management.system.service.impl;

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
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class EmployeeInvoiceServiceImpl implements EmployeeInvoiceService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeInvoiceRepository employeeInvoiceRepository;
    private final RequestedVacationRepository requestedVacationRepository;
    private final EmployeeInvoiceMapper employeeInvoiceMapper;
    private final DayOffDayRepository dayOffDayRepository;


    @Transactional
    @Override
    public void calculateMonthlySalary(int year, int month) {


        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        List<Employee> employeeList = employeeRepository.findEmployeeByStatus(EmployeeStatusEnum.CREATED).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        List<DayOffDay> holidays = dayOffDayRepository.findHolidayByYearAndMonth(year, month);
        int holidayCount = holidays.size();


        int weekendDayCount = 0;
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            DayOfWeek dayOfWeek = LocalDate.of(year, month, i).getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendDayCount++;
            }
        }

        for (Employee employee : employeeList) {

            boolean exists = employeeInvoiceRepository.existsByEmployeeIdAndYearAndMonth(employee.getId(), year, month);

            if (exists) {
                throw new BadRequestException("Invoice already exists for this month");
            }

            List<RequestedVacation> vacations = requestedVacationRepository.findRequestedVacationByEmployeeIdAndStatus(
                    employee.getId(),
                    RequestVacationStatusEnum.APPROVED);

            long workingDaysCount = calculateWorkingDays(yearMonth, holidays, vacations, startOfMonth, endOfMonth);

            BigDecimal workingDay = BigDecimal.valueOf(workingDaysCount);

            BigDecimal baseSalary = employee.getSalary();

            BigDecimal dailySalary = baseSalary.divide(workingDay, 2, RoundingMode.HALF_UP);

            BigDecimal dailyVacationSalary = dailySalary.multiply(new BigDecimal("0.75"));

            BigDecimal vacationSalary = vacations.stream().map(v -> {
                LocalDate vacationStart = v.getStartDay().isBefore(startOfMonth) ? startOfMonth : v.getStartDay();
                LocalDate vacationEnd = v.getEndDay().isAfter(endOfMonth) ? endOfMonth : v.getEndDay();

                Set<LocalDate> vacationDaysSet = vacationStart.datesUntil(vacationEnd.plusDays(1)).filter(d -> {
                    DayOfWeek dow = d.getDayOfWeek();
                    return dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY &&
                            holidays.stream().map(DayOffDay::getHoliday).noneMatch(h -> h.equals(d));
                }).collect(Collectors.toSet());

                return dailyVacationSalary.multiply(BigDecimal.valueOf(vacationDaysSet.size()));
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

            EmployeeInvoice employeeInvoice = new EmployeeInvoice();
            employeeInvoice.setEmployee(employee);
            employeeInvoice.setYear(year);
            employeeInvoice.setMonth(month);
            employeeInvoice.setBaseSalary(baseSalary);
            employeeInvoice.setVacationSalary(vacationSalary);
            employeeInvoice.setTotalSalary(baseSalary.add(vacationSalary));
            employeeInvoice.setCreatedAt(LocalDateTime.now());

            employeeInvoiceRepository.save(employeeInvoice);

        }
    }

    @Override
    public List<RespEmployeeInvoice> getInvoicesByEmployeeId(Long employeeId) {

        List<EmployeeInvoice> invoices = employeeInvoiceRepository.findByEmployeeId(employeeId).orElseThrow(() -> new NotFoundException("Invoices not found"));
        return invoices.stream().map(employeeInvoiceMapper::toResponse).toList();

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
}
