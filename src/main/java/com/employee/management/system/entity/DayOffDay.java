package com.employee.management.system.entity;

import com.employee.management.system.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String name;
    private Integer year;
    private Integer month;
    private int holiday;
}
