package com.employee.management.system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReqEmployee {
    @NotBlank(message = "Name is cannot be empty")
    private String name;

    @NotBlank(message = "Surname is cannot be empty")
    private String surname;

    @NotBlank(message = "Father`s name is cannot be empty")
    private String fathersName;

    @NotBlank(message = "Mail address is cannot be empty")
    @Email(message = "Please enter your email address correctly")
    private String mailAddress;

    @Pattern(regexp = "\\+994\\d{9}", message = "Phone number must be in this format : +994XXXXXXXXX ")
    private String phoneNumber;

    @PastOrPresent(message = "Birthdate is cannot be future time")
    private LocalDate birthDate;

    private LocalDate startWorkDate;

    @Positive(message = "Salary must be positive")
    private BigDecimal salary;

    @Positive(message = "Age must be positive")
    private Integer age;

    private String position;

    @Positive(message = "Vacation is must be positive")
    private Long totalVacation;


}
