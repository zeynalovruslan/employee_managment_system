package com.employee.management.system.service;

import com.employee.management.system.dto.response.RespEmployeeInvoice;

import java.util.List;

public interface EmployeeInvoiceService {

     void calculateMonthlySalary(int year, int month);

      List<RespEmployeeInvoice> getInvoicesByEmployeeId(Long employeeId);
}
