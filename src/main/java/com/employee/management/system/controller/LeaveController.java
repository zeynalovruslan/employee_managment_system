package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqLeave;
import com.employee.management.system.service.LeaveService;
import lombok.RequiredArgsConstructor;
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
    public void reviewHourlyLeave(@PathVariable Long leaveId,
                                  @RequestBody ReqLeave request,
                                  Authentication authentication) {
        leaveService.reviewHourlyLeave(leaveId, request, authentication);
    }


}
