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
@RequestMapping(path = "/api/vacations")
public class VacationController {
    private final RequestedVacationService requestedVacationService;


    @GetMapping("/byVacationId/{vacationId}")
    public RespRequestedVacation getRequestedVacationByVacationId(
            @PathVariable @NotNull(message = "Vacation id cannot be empty") Long vacationId) {
        return requestedVacationService.getRequestedVacationByVacationId(vacationId);
    }

    @GetMapping("/byEmployeeId/{employeeId}")
    public List<RespRequestedVacation> getRequestedVacationListByEmployeeId(
            @PathVariable @NotNull(message = "Employee id cannot be empty") Long employeeId) {
        return requestedVacationService.getRequestedVacationByEmployeeId(employeeId);
    }

    @PostMapping
    public void createVacation(@RequestBody @NotNull(message = "Request day cannot be empty") ReqRequestedVacation request) {
        requestedVacationService.createRequestedVacation(request);
    }

    @PutMapping("/{requestedVacationId}")
    public void updateVacationStatus(@PathVariable @NotNull(message = "Employee id cannot be empty") Long requestedVacationId,
                                     @RequestBody @NotNull(message = "Request is cannot be empty") ReqRequestedVacation request) {
        requestedVacationService.updatedRequestedVacationStatus(requestedVacationId, request);
    }

}
