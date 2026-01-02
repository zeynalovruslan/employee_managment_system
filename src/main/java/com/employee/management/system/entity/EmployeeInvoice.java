package com.employee.management.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_invoices", uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "year", "month"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
