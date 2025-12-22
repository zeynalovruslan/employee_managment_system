package com.employee.management.system.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String message) {

        super(message);
    }
}
