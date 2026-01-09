package com.employee.management.system.service;


import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.entity.UserEntity;

import java.util.List;

public interface NotificationService {

    void createNotification(UserEntity user, String message);

    List<RespNotification> getAllNotificationByUserId(Long userId);

    List<RespNotification> getUnreadNotificationByUserId(Long userId);

    void markAsReadFromEmail(Long notificationId, String token);
}
