package com.employee.management.system.service;


import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.entity.UserEntity;

import java.util.List;

public interface NotificationService {

    void createNotification(UserEntity sender, Long employeeUserId, String message);

    List<RespNotification> getAllNotificationByEmployeeId(Long userId);

    List<RespNotification> getUnreadNotificationByEmployeeId(Long userId);

    void markAsReadFromEmail(Long notificationId, String token);
}
