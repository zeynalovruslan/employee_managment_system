package com.employee.management.system.service;

import com.employee.management.system.enums.RequestVacationStatusEnum;

public interface RequestedVacationService {

    void createRequestedVacation(Long id, Long requestDay);

    void updatedRequestedVacationStatus(Long requestedVacationId, RequestVacationStatusEnum status);
}
