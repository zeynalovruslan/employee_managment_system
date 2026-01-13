package com.employee.management.system.repository;

import com.employee.management.system.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiver_Id(Long userId);

    List<Notification> findByReceiver_IdAndIsReadFalse(Long userId);

    Long countByReceiver_IdAndIsReadFalse(Long userId);

    Long countByReceiver_Id(Long userId);

    Optional<Notification> findById(Long notificationId);
}
