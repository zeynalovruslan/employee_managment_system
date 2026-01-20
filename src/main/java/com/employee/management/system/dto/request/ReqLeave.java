package com.employee.management.system.dto.request;

import com.employee.management.system.enums.LeaveStatusEnum;
import com.employee.management.system.enums.LeaveTypeEnum;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReqLeave {
    private String reason;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LeaveTypeEnum leaveType;
    private LeaveStatusEnum requestStatus;
    private String comment;
}