package com.employee.management.system.service.impl;

import com.employee.management.system.dto.request.ReqLeave;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.Leave;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.enums.EmployeeStatusEnum;
import com.employee.management.system.enums.LeaveStatusEnum;
import com.employee.management.system.enums.LeaveTypeEnum;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.LeaveRepository;
import com.employee.management.system.repository.UserRepository;
import com.employee.management.system.service.LeaveService;
import com.employee.management.system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final NotificationService notificationService;


    @Override
    public void createHourlyLeave(ReqLeave request, Authentication authentication) {

        String username = authentication.getName();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        UserEntity user = userRepository.findByUsername(username).orElseThrow(()
                -> new NotFoundException("User not found"));

        Employee employee = employeeRepository.findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE).orElseThrow(()
                -> new EmployeeNotFoundException("Employee not found"));

        if (request.getStartAt() == null || request.getEndAt() == null) {
            throw new BadRequestException("from/to cannot be null");
        }

        if (!request.getStartAt().toLocalDate().equals(request.getEndAt().toLocalDate())) {
            throw new BadRequestException("Hourly leave must be within the same day");
        }

        if (request.getStartAt().toLocalTime().isBefore(startTime) || request.getStartAt().toLocalTime().isAfter(endTime)
                || request.getEndAt().toLocalTime().isBefore(startTime) || request.getEndAt().toLocalTime().isAfter(endTime)) {
            throw new BadRequestException("Hourly leave must be within 09:00 - 18:00");
        }

        if (!request.getEndAt().isAfter(request.getStartAt())) {
            throw new BadRequestException("Invalid time range");
        }

        boolean overlapExists = employee.getLeaves().stream().filter(leave -> (
                        leave.getRequestStatus() == LeaveStatusEnum.PENDING ||
                                leave.getRequestStatus() == LeaveStatusEnum.APPROVED) &&
                        leave.getLeaveType() == LeaveTypeEnum.HOURLY)
                .anyMatch(leave ->
                        leave.getStartAt().isBefore(request.getEndAt()) &&
                                leave.getEndAt().isBefore(request.getStartAt()));

        if (overlapExists) {
            throw new BadRequestException("Hourly leave already exists");
        }

        Duration diff = Duration.between(request.getStartAt(), request.getEndAt());

        if (diff.compareTo(Duration.ofHours(3)) > 0) {
            throw new BadRequestException("It can't be more than 3 hours.");
        }

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setUser(user);
        leave.setStartAt(request.getStartAt());
        leave.setEndAt(request.getEndAt());
        leave.setReason(request.getReason());
        leave.setRequestStatus(LeaveStatusEnum.PENDING);
        leave.setLeaveType(LeaveTypeEnum.HOURLY);
        leaveRepository.save(leave);
    }


    @Override
    public void reviewHourlyLeave(Long leaveId,
                                  ReqLeave request,
                                  Authentication authentication) {
        String notificationMessage = "Your hourly permission request has been processed";

        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username).orElseThrow(()
                -> new NotFoundException("User not found"));

        Leave leave = leaveRepository.findById(leaveId).orElseThrow(()
                -> new NotFoundException("Leave not found"));

        Employee employee = leave.getEmployee();

        if (leave.getRequestStatus() != LeaveStatusEnum.PENDING) {
            throw new BadRequestException("Hourly leave must be pending.");
        }

        if (request.getRequestStatus() != LeaveStatusEnum.APPROVED &&
                request.getRequestStatus() != LeaveStatusEnum.REJECTED) {
            throw new BadRequestException("Invalid request data");
        }

        notificationService.createNotification(user, employee.getUser().getId(), notificationMessage);

        leave.setRequestStatus(request.getRequestStatus());
        leave.setComment(request.getComment());
        leaveRepository.save(leave);
    }

    @Override
    public void submitAbsenceJustification(ReqLeave request, Authentication authentication) {

        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username).orElseThrow(()
                -> new NotFoundException("User not found"));

        Employee employee = employeeRepository.findByUser_IdAndStatus(user.getId(), EmployeeStatusEnum.ACTIVE)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        if (request.getStartAt() == null || request.getEndAt() == null) {
            throw new BadRequestException("from/to cannot be null");
        }

        LocalDate from = request.getStartAt().toLocalDate();
        LocalDate to = request.getEndAt().toLocalDate();


        if (to.isBefore(from)) {
            throw new BadRequestException("Invalid time range");
        }

        boolean overlapExists = employee.getLeaves().stream()
                .filter(leave ->
                        leave.getRequestStatus() == LeaveStatusEnum.PENDING ||
                                leave.getRequestStatus() == LeaveStatusEnum.APPROVED &&
                                        leave.getLeaveType() == LeaveTypeEnum.ABSENCE
                )
                .anyMatch(leave ->
                        !leave.getEndAt().isBefore(from.atStartOfDay()) &&
                                !leave.getStartAt().isAfter(to.plusDays(1).atStartOfDay())
                );


        if (overlapExists) {
            throw new BadRequestException("Leave dates overlap with existing leave");
        }

        long days = ChronoUnit.DAYS.between(from, to) + 1;
        if (days > 15) {
            throw new BadRequestException("It can't be more than 15 days");
        }

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setUser(user);
        leave.setStartAt(from.atStartOfDay());
        leave.setEndAt(to.plusDays(1).atStartOfDay());
        leave.setReason(request.getReason());
        leave.setRequestStatus(LeaveStatusEnum.PENDING);
        leave.setLeaveType(LeaveTypeEnum.ABSENCE);
        leaveRepository.save(leave);


    }


}
