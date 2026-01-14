package com.employee.management.system.service;

import com.employee.management.system.dto.request.ReqRequestedVacation;
import com.employee.management.system.dto.response.RespRequestedVacation;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RequestedVacationService {

    RespRequestedVacation createRequestedVacation(ReqRequestedVacation request , Authentication authentication);

    boolean updatedRequestedVacationStatus(Long requestedVacationId,
                                           ReqRequestedVacation request,
                                           Authentication authentication);

    RespRequestedVacation getRequestedVacationByVacationId(Long vacationId);

    List<RespRequestedVacation> getRequestedVacationByEmployeeId(Long employeeId);

    RespRequestedVacation calculateVacationPayByVacationId(Long requestedVacationId);
}
