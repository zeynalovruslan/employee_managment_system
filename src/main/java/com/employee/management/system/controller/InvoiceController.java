package com.employee.management.system.controller;

import com.employee.management.system.dto.response.RespEmployeeInvoice;
import com.employee.management.system.service.EmployeeInvoiceService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class InvoiceController {

    @Autowired
    EmployeeInvoiceService employeeInvoiceService;


    @PostMapping("/monthly-calculation")
    public void calculateMonthlyInvoice(@RequestParam int year,
                                        @RequestParam int month) {
        employeeInvoiceService.calculateMonthlySalary(year, month);
    }

    @GetMapping("/employee/{employeeId}/invoices")
    public List<RespEmployeeInvoice> getVacationsByEmployeeId
            (@PathVariable @NotNull(message = "Employee id cannot be empty") Long employeeId) {
        return employeeInvoiceService.getVacationsByEmployeeId(employeeId);
    }

}
