package com.employee.management.system.dto.request;

import com.employee.management.system.enums.RequestVacationStatusEnum;
import lombok.Data;

@Data
public class ReqRequestedVacation {

    private Long requestDay;
    private RequestVacationStatusEnum status;
    private Long employeeId;
}
