package com.employee.management.system.service.impl;

import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.RequestedVacationRepository;
import com.employee.management.system.service.RequestedVacationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestedVacationServiceImpl implements RequestedVacationService {

    private final EmployeeRepository employeeRepository;
    private final RequestedVacationRepository requestedVacationRepository;


    public void createRequestedVacation(Long employeeId, Long requestDay) {
        RequestedVacation requestedVacation = new RequestedVacation();


        if (requestDay <= 0) {
            throw new BadRequestException("Request day must be greater than 0");
        }


        Employee employee = employeeRepository.findEmployeeByIdAndStatus(employeeId, EmployeeStatusEnum.ACTIVE).orElseThrow(
                () -> new EmployeeNotFoundException("Employee is not found"));

        if (requestDay > employee.getTotalVacation()) {
            throw new BadRequestException("Request day must be less than total vacation");
        }


        requestedVacation.setRequestDay(requestDay);
        requestedVacation.setEmployee(employee);
        requestedVacation.setStatus(RequestVacationStatusEnum.PENDING);

        if (employee.getRemainingVacation() == null) {
            employee.setRemainingVacation(0L);
        }

        Long result = employee.getTotalVacation() - requestDay;
        Long resultRemainingDay = employee.getRemainingVacation() + requestDay;

        employee.setTotalVacation(result);
        employee.setRemainingVacation(resultRemainingDay);

        requestedVacationRepository.save(requestedVacation);

    }

    public void UpdatedRequestedVacationStatus(Long requestedVacationId, RequestVacationStatusEnum status) {

        RequestedVacation requestedVacation = requestedVacationRepository.findRequestedVacationByIdAndStatus(requestedVacationId, status).orElseThrow(()
                -> new BadRequestException("Requested vacation is not found"));
        requestedVacation.setStatus(status);
        requestedVacationRepository.save(requestedVacation);

    }


}
