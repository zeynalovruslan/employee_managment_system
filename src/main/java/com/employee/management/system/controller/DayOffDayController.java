package com.employee.management.system.controller;

import com.employee.management.system.dto.request.ReqDayOffDay;
import com.employee.management.system.dto.response.RespDayOffDay;
import com.employee.management.system.service.DayOffDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/holidays")
public class DayOffDayController {
    private final DayOffDayService dayOffDayService;

    @PostMapping
    public void create(@RequestBody ReqDayOffDay request) {
        dayOffDayService.createHoliday(request);
    }

    @GetMapping
    public List<RespDayOffDay> getHolidays(@RequestParam int year,
                                           @RequestParam int month) {
        return dayOffDayService.getHolidayList(year, month);
    }


}
