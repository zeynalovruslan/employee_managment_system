package com.employee.management.system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RespRequestedVacation {

    private Long id;
    private Long employeeId;
    private Long requestDay;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
