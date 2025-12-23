package com.employee.management.system.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RespEmployee {
    private Long id;
    private String name;
    private String surname;
    private String fathersName;
    private String mailAddress;
    private String phoneNumber;
    private String status;
    private LocalDate birthDate;
    private LocalDate startWorkDate;
    private Double salary;
    private Long totalVacation;
    private Long usingVacation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
