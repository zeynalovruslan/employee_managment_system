package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqRequestedVacation;
import com.employee.management.system.dto.response.RespRequestedVacation;
import com.employee.management.system.service.RequestedVacationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class VacationController {
    private final RequestedVacationService requestedVacationService;


    @GetMapping("/vacations/{vacationId}")
    public RespRequestedVacation getRequestedVacationByVacationId(
            @PathVariable
            @NotNull(message = "Vacation id cannot be empty") Long vacationId) {
        return requestedVacationService.getRequestedVacationByVacationId(vacationId);
    }

    @GetMapping("/employees/{employeeId}/vacations")
    public List<RespRequestedVacation> getRequestedVacationListByEmployeeId(
            @PathVariable
            @NotNull(message = "Employee id cannot be empty") Long employeeId) {
        return requestedVacationService.getRequestedVacationByEmployeeId(employeeId);
    }

    @PostMapping("/vacations")
    public RespRequestedVacation createVacation(
            @RequestBody
            @NotNull(message = "Request day cannot be empty") ReqRequestedVacation request) {
        return requestedVacationService.createRequestedVacation(request);
    }

    @PutMapping("/vacations/{requestedVacationId}")
    public void updateVacationStatus(@PathVariable @NotNull(message = "Employee id cannot be empty") Long requestedVacationId,
                                     @RequestBody @NotNull(message = "Request is cannot be empty") ReqRequestedVacation request) {
        requestedVacationService.updatedRequestedVacationStatus(requestedVacationId, request);
    }

    @PostMapping(("/vacation/{requestedVacationId}/calculate-pay"))
    public RespRequestedVacation calculateVacation(
            @PathVariable
            @NotNull(message = "Requested vacation id cannot be empty") Long requestedVacationId) {
        return requestedVacationService.calculateVacationPayByVacationId(requestedVacationId);
    }

}
