package com.employee.management.system.entity;

import com.employee.management.system.audit.Auditable;
import com.employee.management.system.enums.LeaveStatusEnum;
import com.employee.management.system.enums.LeaveTypeEnum;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "leaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leave extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reason;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private LeaveTypeEnum leaveType;

    @Enumerated(EnumType.STRING)
    private LeaveStatusEnum requestStatus;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
