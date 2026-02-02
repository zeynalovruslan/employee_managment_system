package com.employee.management.system.service;

import com.employee.management.system.dto.request.ReqUser;
import org.springframework.security.core.Authentication;

public interface AuthService {

    void createUserForEmployee(ReqUser request);

//    void loginUser(String username, String password);

    void changePasswordForFirstLogin(String newPassword, Authentication auth);
}
