package com.employee.management.system.service;


import com.employee.management.system.dto.response.RespNotification;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface NotificationService {

    void createNotification(Authentication auth, Long employeeId, String message);

    List<RespNotification> getAllNotificationByEmployeeId(Long employeeId);

    List<RespNotification> getUnreadNotificationByEmployeeId(Long employeeId);

    void markAsReadFromEmail(Authentication auth, Long notificationId, String token);
}
