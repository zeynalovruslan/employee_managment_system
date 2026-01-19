package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum LeaveTypeEnum {

    HOURLY("hourly reason"),
    SICK("sick");

    private final String value;

    LeaveTypeEnum(String status) {
        this.value = status;

    }


}
