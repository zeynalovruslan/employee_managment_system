package com.employee.management.system.entity;

import com.employee.management.system.enums.CheckStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "daily_check")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime entryTime;
    private LocalTime exitTime;
    private LocalDate workDate;

    private long lateTime;
    private long overTime;
    private long countWorkedOnHoliday;



    @Enumerated(EnumType.STRING)
    private CheckStatusEnum status;



    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
