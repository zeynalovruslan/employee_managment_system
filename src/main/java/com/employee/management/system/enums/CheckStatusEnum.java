package com.employee.management.system.enums;

public enum CheckStatusEnum {

    ON_TIME("EMPLOYEE ARRIVED ON TIME"),
    LATE("EMPLOYEE IS DDELAYED"),
    ABSENT("EMPLOYEE HAS NOT COME TO WORK");

    private final String value;

    CheckStatusEnum(String status) {
        this.value = status;
    }

}
