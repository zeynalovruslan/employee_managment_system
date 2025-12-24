package com.employee.management.system.service.impl;

import com.employee.management.system.dto.response.RespEmployeeInvoice;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.EmployeeInvoice;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.mapper.EmployeeInvoiceMapper;
import com.employee.management.system.repository.EmployeeInvoiceRepository;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.RequestedVacationRepository;
import com.employee.management.system.service.EmployeeInvoiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service

@RequiredArgsConstructor
public class EmployeeInvoiceServiceImpl implements EmployeeInvoiceService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeInvoiceRepository employeeInvoiceRepository;
    private final RequestedVacationRepository requestedVacationRepository;
    private final EmployeeInvoiceMapper employeeInvoiceMapper;

    @Transactional
    @Override
    public void calculateMonthlySalary(int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();


        List<Employee> employeeList = employeeRepository.findEmployeeByStatus(
                EmployeeStatusEnum.CREATED).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        for (Employee employee : employeeList) {

            boolean exists = employeeInvoiceRepository
                    .existsByEmployeeIdAndYearAndMonth(employee.getId(), year, month);

            if (exists) {
                throw new BadRequestException("Invoice already exists for this month");
            }

            BigDecimal baseSalary = employee.getSalary();

            BigDecimal dailySalary = baseSalary.divide(
                    BigDecimal.valueOf(yearMonth.lengthOfMonth()),
                    RoundingMode.HALF_UP);

            BigDecimal dailyVacationSalary = dailySalary.multiply(new BigDecimal(0.75));


            BigDecimal vacationSalary = requestedVacationRepository.findRequestedVacationByEmployeeIdAndStatus(employee.getId(),
                    RequestVacationStatusEnum.APPROVED).stream().map(requestedVacation -> {
                LocalDate vacationStart = requestedVacation.getStartDay().isBefore(startOfMonth) ? startOfMonth : requestedVacation.getStartDay();

                LocalDate vacationEnd = requestedVacation.getEndDay().isAfter(endOfMonth) ? endOfMonth : requestedVacation.getEndDay();

                if (vacationStart.isAfter(vacationEnd)) {
                    return BigDecimal.ZERO;
                }

                long vacationDays = ChronoUnit.DAYS.between(vacationStart, vacationEnd) + 1;

                return dailyVacationSalary.multiply(BigDecimal.valueOf(vacationDays));
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
    public List<RespEmployeeInvoice> getVacationsByEmployeeId(Long employeeId) {

        List<EmployeeInvoice> invoices = employeeInvoiceRepository.findByEmployeeId(employeeId).orElseThrow(()
                -> new NotFoundException("Invoices not found"));
        return invoices.stream().map(employeeInvoiceMapper::toResponse).toList();

    }


}
