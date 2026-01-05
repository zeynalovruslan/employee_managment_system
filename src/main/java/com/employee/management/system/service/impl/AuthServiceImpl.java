package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqUser;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.Role;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.RoleRepository;
import com.employee.management.system.repository.UserRepository;
import com.employee.management.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Override
    public void createUserForEmployee(ReqUser request) {

        Employee employee = employeeRepository.findEmployeeByIdAndStatus(
                request.getEmployeeId(), EmployeeStatusEnum.CREATED).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));


        boolean existsEmployee = employeeRepository.existsById(employee.getId());
        if (existsEmployee) {
            throw new BadRequestException("Employee already exists");
        }

        boolean existsUserName = userRepository.existsByUsername(request.getUsername());
        if (existsUserName) {
            throw new BadRequestException("Username already exists");
        }

        Set<Role> roles = request.getRoleName().stream().map(roleName -> roleRepository.
                findByRoleName(roleName.toUpperCase()).orElseThrow(()
                        -> new NotFoundException("Role not found"))).collect(Collectors.toSet());

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmployee(employee);
        user.setRoles(roles);

        userRepository.save(user);
    }


    public void loginUser(String username, String password) {



    }

}
