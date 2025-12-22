package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum RequestVacationStatusEnum {

    PENDING("The request is awaiting confirmation."),
    APPROVED("The request is approved."),
    REJECTED("The request is rejected."),
    CANCELLED("You withdrew your request.");

    private final String status;

    RequestVacationStatusEnum(String status) {
        this.status = status;
    }


}
