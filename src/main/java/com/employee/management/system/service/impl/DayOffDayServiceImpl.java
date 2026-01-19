package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqDayOffDay;
import com.employee.management.system.dto.response.RespDayOffDay;
import com.employee.management.system.entity.DayOffDay;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.DayOffDayRepository;
import com.employee.management.system.service.DayOffDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DayOffDayServiceImpl implements DayOffDayService {

    private final DayOffDayRepository dayOffDayRepository;

    @Override
    public void createHoliday(ReqDayOffDay request) {
        DayOffDay dayOffDay = new DayOffDay();
        dayOffDay.setYear(request.getYear());
        dayOffDay.setMonth(request.getMonth());
        dayOffDay.setName(request.getName());
        dayOffDay.setHoliday(request.getHoliday());
        dayOffDayRepository.save(dayOffDay);

    }

    @Override
    public List<RespDayOffDay> getHolidayList(int year, int month) {

        List<RespDayOffDay> holidays = dayOffDayRepository.findHolidayByYearAndMonth(year, month).stream().
                map(holiday ->
                {
                    RespDayOffDay response = new RespDayOffDay();
                    response.setYear(holiday.getYear());
                    response.setMonth(holiday.getMonth());
                    response.setName(holiday.getName());
                    response.setHoliday(holiday.getHoliday());
                    return response;

                }).toList();

        if (holidays.isEmpty()) {
            throw new NotFoundException("Holiday not found");
        }
        return holidays;
    }
}
