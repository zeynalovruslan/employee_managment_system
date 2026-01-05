package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqUser;
import com.employee.management.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public void createUser(ReqUser reqUser) {
        authService.createUserForEmployee(reqUser);
    }


}
