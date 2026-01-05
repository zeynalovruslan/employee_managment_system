package com.employee.management.system.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RespEmployeeInvoice {
    private Long id;
    private Long employeeId;
    private int year;
    private int month;
    private BigDecimal totalSalary;
    private BigDecimal vacationSalary;
    private BigDecimal baseSalary;
    private long overTime;
    private long lateTime;
    private BigDecimal overTimeSalary;
    private BigDecimal lateTimeSalary;
    private long absentDayCount;
    private BigDecimal absentDayPenalty;
    private long countWorkedHoliday;
    private BigDecimal workedHolidaySalary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
