package com.bibliotech.notifications.service;


import com.bibliotech.notifications.notifier.Notifier;

public interface EmailService extends Notifier {
    void sendEmail(String to, String subject, String message);
}
