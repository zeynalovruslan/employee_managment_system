package com.employee.management.system.repository;

import com.employee.management.system.entity.Role;
import com.employee.management.system.enums.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleNameEnum nameEnum);

}
