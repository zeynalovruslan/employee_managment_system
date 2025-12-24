package com.employee.management.system.service;

import com.employee.management.system.dto.response.RespEmployeeInvoice;
import com.employee.management.system.entity.EmployeeInvoice;

import java.util.List;

public interface EmployeeInvoiceService {

     void calculateMonthlySalary(int year, int month);

      List<RespEmployeeInvoice> getVacationsByEmployeeId(Long employeeId);
}
