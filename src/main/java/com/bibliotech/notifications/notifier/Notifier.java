package com.bibliotech.notifications.notifier;

import com.bibliotech.notifications.entity.Notification;
import jakarta.validation.Valid;

public interface Notifier {
    void sendNotification(@Valid Notification notification);
}
