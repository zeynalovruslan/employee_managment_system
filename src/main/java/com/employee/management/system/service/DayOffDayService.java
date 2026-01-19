package com.employee.management.system.service;

import com.employee.management.system.dto.request.ReqDayOffDay;
import com.employee.management.system.dto.response.RespDayOffDay;

import java.util.List;

public interface DayOffDayService {

    void createHoliday(ReqDayOffDay request);

     List<RespDayOffDay> getHolidayList(int year, int month);

}
