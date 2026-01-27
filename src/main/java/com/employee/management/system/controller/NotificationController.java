package com.employee.management.system.controller;

import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{employeeId}")
    public List<RespNotification> getAllNotificationByEmployeeId(@PathVariable Long employeeId) {
        return notificationService.getAllNotificationByEmployeeId(employeeId);
    }

    @GetMapping("/unread-notification/{employeeId}")
    public List<RespNotification> getUnreadNotificationByEmployeeId(@PathVariable Long employeeId) {
        return notificationService.getUnreadNotificationByEmployeeId(employeeId);
    }

    @GetMapping("/read/{notificationId}")
    public void readFromEmail(
            @PathVariable Long notificationId,
            @RequestParam String token,
            Authentication auth) {
        notificationService.markAsReadFromEmail(auth, notificationId, token);
    }

    @PostMapping
    public void createNotification(@RequestParam Long employeeId,
                                   @RequestBody String message,
                                   Authentication auth) {
        notificationService.createNotification(auth, employeeId, message);
    }


}
