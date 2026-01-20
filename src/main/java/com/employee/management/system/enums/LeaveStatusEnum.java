package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum LeaveStatusEnum {
    PENDING("The request is awaiting confirmation."),
    APPROVED("The request is approved."),
    REJECTED("The request is rejected.");

    private final String value;


    LeaveStatusEnum(String s) {
        this.value = s;

    }
}
