package com.employee.management.system.entity;

import com.employee.management.system.enums.EmployeeStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String fathersName;
    private String mailAddress;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private EmployeeStatusEnum status;
    private LocalDate birthDate;
    private LocalDate startWorkDate;


    private Double salary;

    private Integer age;

    private Long totalVacation;

    private Long usingVacation;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestedVacation> requestedVacations;


}
