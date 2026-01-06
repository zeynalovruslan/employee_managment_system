package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqUser;
import com.employee.management.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public void createUser(@RequestBody ReqUser reqUser) {
        authService.createUserForEmployee(reqUser);
    }

    @PostMapping("/login")
    public void login(@RequestParam String username,
                      @RequestParam String password) {
        authService.loginUser(username,password);
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestParam String password){
        authService.changePasswordForFirstLogin(password);
    }

}
