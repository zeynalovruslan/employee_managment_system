package com.employee.management.system.dto.request;

import com.employee.management.system.enums.RequestVacationStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReqRequestedVacation {

    private RequestVacationStatusEnum status;
    private Long employeeId;
    private LocalDate startDay;
    private LocalDate endDay;
}
