package com.employee.management.system.entity;
import com.employee.management.system.audit.Auditable;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.Leave;
import com.employee.management.system.entity.Notification;
import com.employee.management.system.entity.Role;
import jakarta.persistence.*;
import lombok.*;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class UserEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    @ToString.Exclude
    private Employee employee;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Leave> leaves;

    @OneToMany(mappedBy = "sender")
    @ToString.Exclude
    private Set<Notification> sentNotifications = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    @ToString.Exclude
    private Set<Notification> receivedNotifications = new HashSet<>();

    private boolean mustChangePassword = true;
}
