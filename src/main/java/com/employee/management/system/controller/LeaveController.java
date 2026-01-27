package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqLeave;
import com.employee.management.system.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/leaves")
public class LeaveController {
    private final LeaveService leaveService;

    @PostMapping
    public void createHourlyLeave(@RequestBody ReqLeave request,
                                  Authentication auth) {
        leaveService.createHourlyLeave(request, auth);
    }

    @PutMapping("/{leaveId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public void reviewHourlyLeave(@PathVariable Long leaveId,
                                  @RequestBody ReqLeave request,
                                  Authentication authentication) {
        leaveService.reviewHourlyLeave(leaveId, request, authentication);
    }

    @PostMapping("/absence-justification")
    public void submitAbsenceJustification(@RequestBody ReqLeave request,
                                           Authentication authentication) {
        leaveService.submitAbsenceJustification(request, authentication);
    }

    @PutMapping("/absence-justification/review/{leaveId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public void reviewAbsenceJustification(@PathVariable Long leaveId,
                                           @RequestBody ReqLeave request,
                                           Authentication authentication) {
        leaveService.reviewAbsenceJustification(leaveId, request, authentication);
    }


}
