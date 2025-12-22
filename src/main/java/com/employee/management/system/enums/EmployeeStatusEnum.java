package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum EmployeeStatusEnum {


    CREATED("CREATED"),
    ACTIVE("ACTIVE"),
    TERMINATED("TERMINATED"),
    VACATION("VACATION"),
    SUSPENDED("SUSPENDED");

    private final String value;


    EmployeeStatusEnum(String status) {
        this.value = status;
    }
}
