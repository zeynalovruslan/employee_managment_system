package com.employee.management.system.controller;

import com.employee.management.system.service.RequestedVacationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/vacations")
public class VacationController {
    private final RequestedVacationService requestedVacationService;


    @PostMapping
    public void createVacation(@RequestParam @NotNull(message = "Employee id cannot be empty") Long employeeId,
                               @RequestParam @NotNull(message = "Request day cannot be empty") Long requestDay) {
        requestedVacationService.createRequestedVacation(employeeId, requestDay);
    }


}
