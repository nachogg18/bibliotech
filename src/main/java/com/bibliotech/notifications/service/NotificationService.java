package com.bibliotech.notifications.service;

import com.bibliotech.notifications.entity.Notification;
import com.bibliotech.notifications.notifier.Notifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Notifier notifier;

    public void notifyUser(Notification notification) {
        notifier.sendNotification(notification);
    }
}