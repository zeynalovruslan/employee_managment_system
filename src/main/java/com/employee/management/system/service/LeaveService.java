package com.employee.management.system.service;

import com.employee.management.system.dto.request.ReqLeave;
import org.springframework.security.core.Authentication;

public interface LeaveService {
    void createHourlyLeave(ReqLeave request, Authentication auth);

    void reviewHourlyLeave(Long leaveId, ReqLeave request,
                           Authentication authentication);

    void submitAbsenceJustification(ReqLeave request, Authentication authentication);
}
