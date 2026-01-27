package com.employee.management.system.controller;

import com.employee.management.system.dto.response.RespEmployeeInvoice;
import com.employee.management.system.service.EmployeeInvoiceService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public void calculateMonthlyInvoice(@RequestParam int year,
                                        @RequestParam int month,
                                        @RequestBody String message,
                                        Authentication authentication) {
        employeeInvoiceService.calculateMonthlySalary(year, month, message, authentication);
    }

    @GetMapping("/employee/{employeeId}/invoices")
    public List<RespEmployeeInvoice> getInvoicesByEmployeeId
            (@PathVariable @NotNull(message = "Employee id cannot be empty") Long employeeId) {
        return employeeInvoiceService.getInvoicesByEmployeeId(employeeId);
    }

}
