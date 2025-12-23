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
import org.springframework.stereotype.Service;

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
    public List<RespRequestedVacation> getRequestedVacationByEmployeeId(Long employeeId) {
        List<RequestedVacation> vacations = requestedVacationRepository.findRequestedVacationByEmployeeId(employeeId);
        if (vacations.isEmpty()) {
            throw new NotFoundException("Vacation is not Found");
        }
        return vacations.stream().map(requestedVacationMapper::toResponse).toList();
    }


    @Override
    public void createRequestedVacation(ReqRequestedVacation request) {

        if (request.getRequestDay() <= 0) {
            throw new BadRequestException("Request day must be greater than 0");
        }
        Employee employee = employeeRepository.findEmployeeByIdAndStatus(request.getEmployeeId(), EmployeeStatusEnum.CREATED).orElseThrow(
                () -> new EmployeeNotFoundException("Employee is not found"));

        if (request.getRequestDay() > employee.getTotalVacation()) {
            throw new BadRequestException("Request day must be less than total vacation");
        }
        List<RequestedVacation> requestedVacations = employee.getRequestedVacations();

        Long count = requestedVacations.stream().filter(
                v -> v.getStatus() == RequestVacationStatusEnum.PENDING).filter
                (v -> v.getRequestDay() != null).mapToLong(RequestedVacation::getRequestDay).sum();

        Long remainingVacation = employee.getTotalVacation() - count;

        if (request.getRequestDay() > remainingVacation) {
            throw new BadRequestException("Request day must be less than total vacation");
        }

        RequestedVacation requestedVacation = new RequestedVacation();
        requestedVacation.setRequestDay(request.getRequestDay());
        requestedVacation.setEmployee(employee);
        requestedVacation.setStatus(RequestVacationStatusEnum.PENDING);

        if (employee.getUsingVacation() == null) {
            employee.setUsingVacation(0L);
        }
        requestedVacationRepository.save(requestedVacation);
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


}
