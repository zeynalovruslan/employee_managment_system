package com.employee.management.system.entity;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.enums.RoleNameEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleNameEnum roleName;

    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    private Set<UserEntity> users = new HashSet<>();
}
