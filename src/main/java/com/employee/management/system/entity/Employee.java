package com.employee.management.system.entity;

import com.employee.management.system.enums.EmployeeStatusEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

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

    @Nullable
    private Long totalVacation;


    private Long remainingVacation;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestedVacation> requestedVacations;


}
