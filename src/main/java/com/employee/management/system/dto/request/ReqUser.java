package com.employee.management.system.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ReqUser {
    private Long employeeId;
    private String username;
    private String password;
    private List<String> roleName;
}
