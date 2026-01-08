package com.employee.management.system.entity;

import com.employee.management.system.audit.Auditable;
import com.employee.management.system.enums.EmployeeStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee extends Auditable {
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
    private BigDecimal salary;
    private Integer age;
    private Long totalVacation;
    private Long usingVacation;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestedVacation> requestedVacations;

    @OneToMany(mappedBy = "employee" ,cascade = CascadeType.ALL)
    private List<EmployeeInvoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "employee" ,cascade =CascadeType.ALL )
    private List <DailyCheck> dailyCheck;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy = "employee")
    private UserEntity user;


}
