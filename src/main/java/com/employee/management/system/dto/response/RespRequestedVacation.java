package com.employee.management.system.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RespRequestedVacation {

    private Long id;
    private Long employeeId;
    private Long requestDay;
    private String status;
    private BigDecimal vacationPay;
    private BigDecimal totalSalary;
    private LocalDate startDay;
    private LocalDate endDay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
