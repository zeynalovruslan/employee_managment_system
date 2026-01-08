package com.employee.management.system.security;

import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.enums.RoleNameEnum;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("userSecurity")
public class UserSecurity {
    private final UserRepository userRepository;


    public boolean isDirectorOfDepartment(Long departmentId) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        UserEntity user = userRepository.findByUsername(auth.getName()).orElseThrow(()
                -> new EmployeeNotFoundException("Employee not found"));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(RoleNameEnum.ADMIN));

        if (isAdmin) {
            return true;
        }

        if (user.getEmployee() == null || user.getEmployee().getPosition().getDepartment() == null) {
            return false;
        }
        boolean isDirector = user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(RoleNameEnum.DEPARTMENT_DIRECTOR));

        return isDirector && user.getEmployee().getPosition().getDepartment().getId().equals(departmentId);
    }

    public boolean isOwner(Long employeeId) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        UserEntity user = userRepository.findByUsername(auth.getName()).orElseThrow(()
                -> new EmployeeNotFoundException("Employee not found"));

        boolean isAdminOrHr = user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(RoleNameEnum.ADMIN) ||
                r.getRoleName().equals(RoleNameEnum.HR));
        if (isAdminOrHr) {
            return true;
        }

        if (user.getEmployee() == null) {
            return false;
        }

        boolean isOwner = user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleNameEnum.EMPLOYEE));
        return isOwner && user.getEmployee().getId().equals(employeeId);

    }


}
