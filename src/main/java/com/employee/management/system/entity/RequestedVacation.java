package com.employee.management.system.entity;

import com.employee.management.system.enums.RequestVacationStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "requested_vacations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestedVacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private Long requestDay;

    private LocalDate startDay;

    private LocalDate endDay;

    @Enumerated(EnumType.STRING)
    private RequestVacationStatusEnum status;

    private BigDecimal vacationPay;

    private BigDecimal totalSalary;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


}
