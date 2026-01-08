package com.employee.management.system.service;


import com.employee.management.system.entity.UserEntity;

public interface NotificationService {

    void createNotification(UserEntity user, String message);
}
