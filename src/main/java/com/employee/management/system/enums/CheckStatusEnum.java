package com.employee.management.system.enums;

import lombok.Getter;

@Getter
public enum CheckStatusEnum {

    ON_TIME("EMPLOYEE ARRIVED ON TIME"),
    LATE("EMPLOYEE IS DELAYED"),
    ABSENT("EMPLOYEE HAS NOT COME TO WORK"),
    WORKED_ON_HOLIDAY("EMPLOYEE WORKED ON HOLIDAY OR WEEKEND");

    private final String value;

    CheckStatusEnum(String status) {
        this.value = status;
    }

}
