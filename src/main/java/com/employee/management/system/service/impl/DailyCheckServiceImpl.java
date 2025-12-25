package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqDailyCheck;
import com.employee.management.system.entity.DailyCheck;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.enums.CheckStatusEnum;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.repository.DailyCheckRepository;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.service.DailyCheckService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyCheckServiceImpl implements DailyCheckService {

    private final EmployeeRepository employeeRepository;
    private final DailyCheckRepository dailyCheckRepository;

    @Value("${work.work-start-time}")
    private LocalTime startWorkTime;
    @Value("${work.work-end-time}")
    private LocalTime endWorkTime;


    @Override
    public void addInputAndOutput(ReqDailyCheck request) {

        Employee employee = employeeRepository.findEmployeeById(request.getEmployeeId()).orElseThrow(()
                -> new EmployeeNotFoundException("Employee is not found"));

        DailyCheck dailyCheck = new DailyCheck();

        LocalDate workDate = LocalDate.now();

        long lateMinutes = 0;
        CheckStatusEnum status = CheckStatusEnum.ON_TIME;

        if (request.getEntryTime().isAfter(startWorkTime)) {
            lateMinutes += Duration.between(startWorkTime, request.getEntryTime()).toMinutes();
            status = CheckStatusEnum.LATE;
        }

        if (request.getExitTime().isBefore(endWorkTime)) {
            lateMinutes += Duration.between(request.getExitTime(), endWorkTime).toMinutes();
            status = CheckStatusEnum.LATE;
        }

        long overtimeMinutes = 0;
        if (request.getExitTime().isAfter(endWorkTime)) {
            overtimeMinutes = Duration.between(endWorkTime, request.getExitTime()).toMinutes();
        }

        dailyCheck.setEmployee(employee);
        dailyCheck.setLateTime(lateMinutes);
        dailyCheck.setOverTime(overtimeMinutes);
        dailyCheck.setStatus(status);
        dailyCheck.setEmployee(employee);
        dailyCheck.setEntryTime(request.getEntryTime());
        dailyCheck.setExitTime(request.getExitTime());
        dailyCheck.setWorkDate(workDate);

        dailyCheckRepository.save(dailyCheck);


    }

    @Transactional
    @Scheduled(cron = "0 59 23 * * *")
    public void checkAbsentEmployee() {

        LocalDate workDay = LocalDate.now();

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            boolean exists = dailyCheckRepository.existsDailyCheckByEmployeeAndWorkDate(employee, workDay);

            if (!exists) {
                DailyCheck dailyCheck = new DailyCheck();
                dailyCheck.setEmployee(employee);
                dailyCheck.setWorkDate(workDay);
                dailyCheck.setOverTime(0);
                dailyCheck.setLateTime(0);
                dailyCheck.setEntryTime(null);
                dailyCheck.setExitTime(null);
                dailyCheck.setStatus(CheckStatusEnum.ABSENT);
                dailyCheckRepository.save(dailyCheck);

            }
        }
    }
}
