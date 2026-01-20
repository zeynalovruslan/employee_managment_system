package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum LeaveTypeEnum {

    HOURLY("hourly reason"),
    ABSENCE("absence reason");

    private final String value;

    LeaveTypeEnum(String status) {
        this.value = status;

    }


}
