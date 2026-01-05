package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum RoleNameEnum {

    ADMIN("ADMIN"),
    HR("HR"),
    Employee("EMPLOYEE");

    private final String value;

    RoleNameEnum(String status) {
        this.value = status;
    }


}
