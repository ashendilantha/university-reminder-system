package com.university.reminderapp.repository;

import com.university.reminderapp.model.DeliveryLog;
import com.university.reminderapp.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryLogRepository extends JpaRepository<DeliveryLog, Long> {
    List<DeliveryLog> findByNotification(Notification notification);
    void deleteByNotification(Notification notification);
}
