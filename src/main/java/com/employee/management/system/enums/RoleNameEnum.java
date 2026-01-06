package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum RoleNameEnum {

    ADMIN("ADMIN"),
    HR("HR"),
    Employee("EMPLOYEE"),
    Department_Director("DEPARTMENT_DIRECTOR");

    private final String value;

    RoleNameEnum(String status) {
        this.value = status;
    }


}
