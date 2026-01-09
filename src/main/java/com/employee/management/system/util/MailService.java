package com.employee.management.system.util;

import com.employee.management.system.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;


    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendMail(Notification notification) {

        String link = "https://api.company.com/notifications/read/"
                + notification.getId()
                + "?token="
                + notification.getToken();

        String body = """
                Hello %s,

                %s

                View details:
                %s
                """.formatted(
                notification.getUser().getUsername(),
                notification.getMessage(),
                link);

        send(notification.getUser().getEmployee().getMailAddress(), "New Notification", body);

    }

}
