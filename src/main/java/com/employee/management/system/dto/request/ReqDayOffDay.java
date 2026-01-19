package com.employee.management.system.dto.request;

import lombok.Data;

@Data
public class ReqDayOffDay {
    private String name;
    private Integer year;
    private Integer month;
    private int holiday;
}
