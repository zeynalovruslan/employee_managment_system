package com.employee.management.system.entity;

import com.employee.management.system.audit.Auditable;
import com.employee.management.system.exception.BadRequestException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.LocalDate;

@Entity
@Table(name = "day_off")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayOffDay extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Min(2025)
    @Max(2100)
    private Integer year;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;


    @Min(1)
    @Max(31)
    private int holiday;


    @PrePersist
    @PreUpdate
    private void validateDate() {
        try {
            LocalDate.of(year, month, holiday);
        } catch (DateTimeException e) {
            throw new BadRequestException(
                    "Invalid date: " + year + "-" + month + "-" + holiday
            );
        }
    }
}
