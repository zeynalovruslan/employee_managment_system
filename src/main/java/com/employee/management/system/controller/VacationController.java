package com.employee.management.system.controller;

import com.employee.management.system.enums.RequestVacationStatusEnum;
import com.employee.management.system.service.RequestedVacationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public void updateVacationStatus(@RequestParam @NotNull(message = "Employee id cannot be empty") Long requestedVacationId,
                                     @RequestParam @NotNull(message = "Request is cannot be empty") RequestVacationStatusEnum statusEnum) {
        requestedVacationService.updatedRequestedVacationStatus(requestedVacationId, statusEnum);
    }


}
