package com.employee.management.system.repository;

import com.employee.management.system.entity.Notification;
import com.employee.management.system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByUserAndReadFalse(UserEntity user);
}
