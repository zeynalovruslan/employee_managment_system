package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqDailyCheck;
import com.employee.management.system.service.DailyCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/daily-check")
public class DailyCheckController {

    private final DailyCheckService dailyCheckService;


    @PostMapping
    public void addDailyCheck(@Valid @RequestBody ReqDailyCheck request) {
        dailyCheckService.addInputAndOutput(request);
    }

}
