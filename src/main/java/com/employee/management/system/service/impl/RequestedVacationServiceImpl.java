package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqRequestedVacation;
import com.employee.management.system.dto.response.RespRequestedVacation;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.mapper.RequestedVacationMapper;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.RequestedVacationRepository;
import com.employee.management.system.service.RequestedVacationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestedVacationServiceImpl implements RequestedVacationService {

    private final EmployeeRepository employeeRepository;
    private final RequestedVacationRepository requestedVacationRepository;
    private final RequestedVacationMapper requestedVacationMapper;

    @Override
    public RespRequestedVacation getRequestedVacationByVacationId(Long vacationId) {
        RequestedVacation vacation = requestedVacationRepository.findById(vacationId).orElseThrow(() ->
                new NotFoundException("Vacation is not Found"));
        return requestedVacationMapper.toResponse(vacation);
    }

    @Override
    @PreAuthorize("@userSecurity.isOwner(#employeeId)")
    public List<RespRequestedVacation> getRequestedVacationByEmployeeId(Long employeeId) {
        List<RequestedVacation> vacations = requestedVacationRepository.findRequestedVacationByEmployeeId(employeeId);
        return vacations.stream().map(requestedVacationMapper::toResponse).toList();
    }


    @Override
    public RespRequestedVacation createRequestedVacation(ReqRequestedVacation request) {
        Employee employee = employeeRepository.findEmployeeByIdAndStatus(request.getEmployeeId(), EmployeeStatusEnum.CREATED).orElseThrow(
                () -> new EmployeeNotFoundException("Employee is not found"));

        Long totalDay = ChronoUnit.DAYS.between(request.getStartDay(), request.getEndDay()) + 1;

        if (totalDay > employee.getTotalVacation()) {
            throw new BadRequestException("Request day must be less than total vacation");
        }

        boolean overlapExists = employee.getRequestedVacations().stream()
                .filter(v -> v.getStatus() == RequestVacationStatusEnum.PENDING || v.getStatus() == RequestVacationStatusEnum.APPROVED)
                .anyMatch(v ->
                        !v.getEndDay().isBefore(request.getStartDay()) &&
                                !v.getStartDay().isAfter(request.getEndDay())
                );

        if (overlapExists) {
            throw new BadRequestException("Vacation dates overlap with existing vacation");
        }

        List<RequestedVacation> requestedVacations = employee.getRequestedVacations();

        Long count = requestedVacations.stream().filter(v -> v.getStatus() == RequestVacationStatusEnum.PENDING).filter
                (v -> v.getRequestDay() != null).mapToLong(RequestedVacation::getRequestDay).sum();

        Long remainingVacation = employee.getTotalVacation() - count;

        if (totalDay > remainingVacation) {
            throw new BadRequestException("Request day must be less than total vacation");
        }

        if (request.getStartDay().isAfter(request.getEndDay())) {
            throw new BadRequestException("Start day cannot be after end day");
        }

        RequestedVacation requestedVacation = new RequestedVacation();
        requestedVacation.setRequestDay(totalDay);
        requestedVacation.setEmployee(employee);
        requestedVacation.setStatus(RequestVacationStatusEnum.PENDING);
        requestedVacation.setStartDay(request.getStartDay());
        requestedVacation.setEndDay(request.getEndDay());

        if (employee.getUsingVacation() == null) {
            employee.setUsingVacation(0L);
        }
        requestedVacationRepository.save(requestedVacation);

        return requestedVacationMapper.toResponse(requestedVacation);
    }

    @Override
    public boolean updatedRequestedVacationStatus(Long requestedVacationId, ReqRequestedVacation request) {

        RequestedVacation requestedVacation = requestedVacationRepository.findRequestedVacationByIdAndStatus(requestedVacationId,
                RequestVacationStatusEnum.PENDING).orElseThrow(()
                -> new NotFoundException("Requested vacation is not found"));

        if (request.getStatus().equals(RequestVacationStatusEnum.PENDING)) {
            throw new BadRequestException("The request you sent is the same as the status in the system");
        }

        Employee employee = requestedVacation.getEmployee();

        if (request.getStatus().equals(RequestVacationStatusEnum.APPROVED)) {
            Long result = employee.getTotalVacation() - requestedVacation.getRequestDay();
            Long resultUsingDay = employee.getUsingVacation() + requestedVacation.getRequestDay();
            employee.setUsingVacation(resultUsingDay);
            employee.setTotalVacation(result);
        }

        requestedVacation.setStatus(request.getStatus());

        requestedVacationRepository.save(requestedVacation);
        return true;
    }

    @Override
    public RespRequestedVacation calculateVacationPayByVacationId(Long requestedVacationId) {

        RequestedVacation requestedVacation = requestedVacationRepository.findById(requestedVacationId).orElseThrow(()
                -> new NotFoundException("Vacation is not found"));

        Employee employee = requestedVacation.getEmployee();
        BigDecimal dailySalary = employee.getSalary().divide(new BigDecimal(21));
        BigDecimal dailyVacationPay = dailySalary.multiply(new BigDecimal(0.75));
        BigDecimal totalVacationPay = dailyVacationPay.multiply(new BigDecimal(requestedVacation.getRequestDay()));

        Long remainingDay = 21 - requestedVacation.getRequestDay();
        BigDecimal remainingDaysPay = dailySalary.multiply(new BigDecimal(remainingDay));

        BigDecimal totalSalary = remainingDaysPay.add(totalVacationPay);

        requestedVacation.setTotalSalary(totalSalary);
        requestedVacation.setVacationPay(totalVacationPay);
        requestedVacationRepository.save(requestedVacation);

        return requestedVacationMapper.toResponse(requestedVacation);

    }

}
