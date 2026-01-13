package com.employee.management.system.controller;

import com.employee.management.system.dto.response.RespNotification;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.UserRepository;
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

    private final UserRepository userRepository;

    @GetMapping
    public List<RespNotification> getAllNotificationByUserId(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        UserEntity user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));
        return notificationService.getAllNotificationByUserId(user.getId());
    }

    @GetMapping("/unread-notification")
    public List<RespNotification> getUnreadNotificationByUserId(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        UserEntity user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));
        return notificationService.getUnreadNotificationByUserId(user.getId());
    }

    @GetMapping("/read/{notificationId}")
    public void readFromEmail(
            @PathVariable Long notificationId,
            @RequestParam String token,
            HttpServletResponse response) throws IOException {
        notificationService.markAsReadFromEmail(notificationId, token);}

    @PostMapping
    public void createNotification(@AuthenticationPrincipal UserEntity user,
                                   @RequestParam Long userId,
                                   @RequestBody String message) throws IOException {
        notificationService.createNotification(user, userId,message);
    }


}
