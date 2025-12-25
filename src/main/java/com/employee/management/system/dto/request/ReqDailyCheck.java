package com.employee.management.system.dto.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ReqDailyCheck {
    private Long employeeId;
    private LocalTime entryTime;
    private LocalTime exitTime;
}
