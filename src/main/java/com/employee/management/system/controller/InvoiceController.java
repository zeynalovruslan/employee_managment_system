package com.employee.management.system.controller;

import com.employee.management.system.service.EmployeeInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/invoices")
public class InvoiceController {

    @Autowired
    EmployeeInvoiceService employeeInvoiceService;


    @PostMapping("/monthly-calculation")
    public void calculateMonthlyInvoice(@RequestParam int year,
                                        @RequestParam int month) {
        employeeInvoiceService.calculateMonthlySalary(year, month);
    }


}
