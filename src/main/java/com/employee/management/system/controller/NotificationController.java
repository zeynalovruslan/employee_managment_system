package com.employee.management.system.controller;

import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/notification")
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping
    public List<RespNotification> getAllNotificationByUserId(@AuthenticationPrincipal UserEntity user) {
        return notificationService.getAllNotificationByUserId(user.getId());
    }

    @GetMapping("/unread-notification")
    public List<RespNotification> getUnreadNotificationByUserId(@AuthenticationPrincipal UserEntity user) {
        return notificationService.getUnreadNotificationByUserId(user.getId());
    }

    @GetMapping("/read/{notificationId}")
    public void readFromEmail(
            @PathVariable Long notificationId,
            @RequestParam String token,
            HttpServletResponse response) throws IOException {
        notificationService.markAsReadFromEmail(notificationId, token);
        response.sendRedirect("https://frontend.company.com/notifications");
    }

    @PostMapping
    public void createNotification(@AuthenticationPrincipal UserEntity user, String message) throws IOException {
        notificationService.createNotification(user, message);
    }


}
