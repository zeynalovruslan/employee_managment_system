package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum RoleNameEnum {

    ADMIN("ADMIN"),
    HR("HR"),
    EMPLOYEE("EMPLOYEE"),
    DEPARTMENT_DIRECTOR("DEPARTMENT_DIRECTOR");

    private final String value;

    RoleNameEnum(String status) {
        this.value = status;
    }


}
