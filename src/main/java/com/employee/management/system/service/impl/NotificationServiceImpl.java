package com.employee.management.system.service.impl;

import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.entity.Notification;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.NotificationRepository;
import com.employee.management.system.repository.UserRepository;
import com.employee.management.system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void createNotification(UserEntity user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    public List<RespNotification> getAllNotificationByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));


        List<RespNotification> notificationList = notificationRepository.findByUserId(user.getId()).stream().map(notification -> {

            RespNotification respNotification = new RespNotification();
            respNotification.setMessage(notification.getMessage());
            respNotification.setUserId(user.getId());
            respNotification.setCreatedAt(notification.getCreatedAt());

            return respNotification;

        }).toList();
        return notificationList;
    }


}
