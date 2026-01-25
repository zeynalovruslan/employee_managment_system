package com.employee.management.system.service.impl;

import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.Notification;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.EmployeeNotFoundException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.EmployeeRepository;
import com.employee.management.system.repository.NotificationRepository;
import com.employee.management.system.repository.UserRepository;
import com.employee.management.system.service.NotificationService;
import com.employee.management.system.util.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final EmployeeRepository employeeRepository;


    @Override
    public void createNotification(UserEntity sender, Long employeeUserId, String message) {

        UserEntity user = userRepository.findById(employeeUserId).orElseThrow(()
                -> new NotFoundException("User not found"));


        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setToken(UUID.randomUUID().toString());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReceiver(user);

        notificationRepository.save(notification);

        if (user.getEmployee().getMailAddress() != null) {
            mailService.sendMail(notification);
        }
    }

    @Override
    @PreAuthorize("@userSecurity.isOwner(#employeeId)")
    public List<RespNotification> getAllNotificationByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow((()
                -> new EmployeeNotFoundException("Employee is not found")));

        Long userId = employee.getUser().getId();

        Long totalNotifications = notificationRepository.countByReceiver_Id(userId);

        Long unreadNotifications = notificationRepository.countByReceiver_IdAndIsReadFalse(userId);

        List<RespNotification> notificationList = notificationRepository.findByReceiver_Id(userId).stream().map(notification -> {

            RespNotification respNotification = new RespNotification();
            respNotification.setMessage(notification.getMessage());
            respNotification.setUserId(userId);
            respNotification.setCreatedAt(notification.getCreatedAt());
            respNotification.setRead(notification.isRead());
            respNotification.setTotalNotificationCount(totalNotifications);
            respNotification.setUnreadNotificationCount(unreadNotifications);

            return respNotification;

        }).toList();
        return notificationList;
    }

    @Override
    @PreAuthorize("@userSecurity.isOwner(#employeeId)")
    public List<RespNotification> getUnreadNotificationByEmployeeId(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow((()
                -> new EmployeeNotFoundException("Employee is not found")));

        Long userId = employee.getUser().getId();


        Long unreadNotifications = notificationRepository.countByReceiver_IdAndIsReadFalse(userId);

        List<RespNotification> unreadNotificationList = notificationRepository.findByReceiver_IdAndIsReadFalse(
                userId).stream().map(notification ->
        {
            RespNotification respNotification = new RespNotification();
            respNotification.setUserId(userId);
            respNotification.setMessage(notification.getMessage());
            respNotification.setRead(notification.isRead());
            respNotification.setCreatedAt(notification.getCreatedAt());
            respNotification.setUnreadNotificationCount(unreadNotifications);

            return respNotification;

        }).toList();

        return unreadNotificationList;
    }

    @Override
    public void markAsReadFromEmail(Long notificationId, String token) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()
                -> new NotFoundException("Notification not found"));

        if (notification.getToken() == null || !notification.getToken().equals(token)) {
            throw new BadRequestException("Invalid token");
        }


        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }


}
