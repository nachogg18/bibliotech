package com.bibliotech.notifications.service;

import com.bibliotech.notifications.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSenderImpl emailSender;

    public void sendEmail(
      String to, String subject, String text) {
        
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("bibliotech@gmail.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendNotification(Notification notification) {
        notification.checkMandatoryField();
        sendEmail(notification.getRecipient(), notification.getSubject(), notification.getMessage());
    }
}