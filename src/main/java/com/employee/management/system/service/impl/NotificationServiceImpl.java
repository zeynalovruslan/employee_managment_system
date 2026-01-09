package com.employee.management.system.service.impl;

import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.entity.Notification;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.exception.BadRequestException;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.NotificationRepository;
import com.employee.management.system.repository.UserRepository;
import com.employee.management.system.service.NotificationService;
import com.employee.management.system.util.MailService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void createNotification(UserEntity user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setToken(UUID.randomUUID().toString());
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);

        if (user.getEmployee().getMailAddress() != null) {
            mailService.sendMail(notification);
        }
    }

    @Override
    public List<RespNotification> getAllNotificationByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Long totalNotifications = notificationRepository.countByUserId(userId);

        Long unreadNotifications= notificationRepository.countByUserIdAndIsReadFalse(userId);

        List<RespNotification> notificationList = notificationRepository.findByUserId(user.getId()).stream().map(notification -> {

            RespNotification respNotification = new RespNotification();
            respNotification.setMessage(notification.getMessage());
            respNotification.setUserId(user.getId());
            respNotification.setCreatedAt(notification.getCreatedAt());
            respNotification.setRead(notification.isRead());
            respNotification.setTotalNotificationCount(totalNotifications);
            respNotification.setUnreadNotificationCount(unreadNotifications);

            return respNotification;

        }).toList();
        return notificationList;
    }

    @Override
    public List<RespNotification> getUnreadNotificationByUserId(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));

        Long unreadNotifications= notificationRepository.countByUserIdAndIsReadFalse(userId);

        List<RespNotification> unreadNotificationList = notificationRepository.findByUserIdAndIsReadFalse(
                user.getId()).stream().map(notification ->
        {
            RespNotification respNotification = new RespNotification();
            respNotification.setUserId(user.getId());
            respNotification.setMessage(notification.getMessage());
            respNotification.setRead(notification.isRead());
            respNotification.setCreatedAt(notification.getCreatedAt());
            respNotification.setUnreadNotificationCount(unreadNotifications);

            return respNotification;

        }).toList();

        return unreadNotificationList;
    }

    @Override
    public void markAsReadFromEmail(Long notificationId, String token){

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()
                -> new NotFoundException("Notification not found"));

        if (!notification.getToken().equals(token)){
            throw new BadRequestException("Invalid token");
        }

        if (!notification.isRead()){
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }




}
