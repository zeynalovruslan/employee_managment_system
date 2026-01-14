package com.employee.management.system.service;

import com.employee.management.system.dto.response.RespEmployeeInvoice;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface EmployeeInvoiceService {

     void calculateMonthlySalary(int year, int month, String message, Authentication authentication);

      List<RespEmployeeInvoice> getInvoicesByEmployeeId(Long employeeId);
}
