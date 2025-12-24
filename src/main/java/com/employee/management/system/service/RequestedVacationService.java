package com.employee.management.system.service;

import com.employee.management.system.dto.request.ReqRequestedVacation;
import com.employee.management.system.dto.response.RespRequestedVacation;

import java.util.List;

public interface RequestedVacationService {

    RespRequestedVacation createRequestedVacation(ReqRequestedVacation request);

    boolean updatedRequestedVacationStatus(Long requestedVacationId, ReqRequestedVacation request);

    RespRequestedVacation getRequestedVacationByVacationId(Long vacationId);

    List<RespRequestedVacation> getRequestedVacationByEmployeeId(Long employeeId);

    RespRequestedVacation calculateVacationPayByVacationId(Long requestedVacationId);
}
