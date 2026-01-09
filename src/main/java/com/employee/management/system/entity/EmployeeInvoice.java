package com.employee.management.system.entity;

import com.employee.management.system.audit.Auditable;
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
public class EmployeeInvoice extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;
    private int month;
    private BigDecimal totalSalary;
    private BigDecimal vacationSalary;
    private BigDecimal baseSalary;
    private BigDecimal overTimeSalary;
    private BigDecimal lateTimeSalary;
    private BigDecimal absentDayPenalty;
    private BigDecimal workedHolidaySalary;
    private long overTime;
    private long lateTime;
    private long countWorkedHoliday;
    private long absentDayCount;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
