package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqUser;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.Role;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.RoleNameEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.RoleRepository;
import com.employee.management.system.repository.UserRepository;
import com.employee.management.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Value("${login.first-password}")
    private String firstLoginPassword;

    @Override
    public void createUserForEmployee(ReqUser request) {

        Employee employee = employeeRepository.findEmployeeByIdAndStatus(
                request.getEmployeeId(), EmployeeStatusEnum.CREATED).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        boolean existsEmployee = userRepository.existsByEmployee(employee);
        if (existsEmployee) {
            throw new BadRequestException("Employee already exists");
        }

        boolean existsUserName = userRepository.existsByUsername(request.getUsername());
        if (existsUserName) {
            throw new BadRequestException("Username already exists");
        }

        Set<Role> roles = request.getRoleName().stream()
                .map(roleName -> {
                    RoleNameEnum enumRole;
                    try {
                        enumRole = RoleNameEnum.valueOf(roleName.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new NotFoundException("Invalid role name: " + roleName);
                    }

                    return roleRepository.findByRoleName(enumRole)
                            .orElseThrow(() -> new NotFoundException("Role not found"));
                })
                .collect(Collectors.toSet());


        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(firstLoginPassword));
        user.setEmployee(employee);
        user.setRoles(roles);
        user.setMustChangePassword(true);

        userRepository.save(user);
    }

    @Override
    public void loginUser(String username, String password) {

        UserEntity user = userRepository.findByUsername(username).orElseThrow(()
                -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Wrong password");
        }

        if (user.isMustChangePassword()) {
            throw new BadRequestException("You must change your password before login");
        }


    }

    @Override
    public void changePasswordForFirstLogin(String newPassword) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username).orElseThrow(()
                -> new NotFoundException("User not found"));

        if (!user.isMustChangePassword()) {
            throw new BadRequestException("Password already changed");
        }


        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException("The password cannot be the same as the first password.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);

        Employee employee = user.getEmployee();
        employee.setStatus(EmployeeStatusEnum.ACTIVE);

        employeeRepository.save(employee);
        userRepository.save(user);

    }

}
