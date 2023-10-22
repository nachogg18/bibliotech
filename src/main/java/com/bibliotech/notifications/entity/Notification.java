package com.bibliotech.notifications.entity;

import com.bibliotech.security.entity.User;
import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.Objects;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String subject;
    private String message;
    private Instant sentDate;
    private boolean sent;
    private String mediaType;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    public void checkMandatoryField() {
        if (Objects.isNull(recipient) || Objects.isNull(subject) || Objects.isNull(message)) {
            throw new ValidationException("Notificacion invalida");
        }
    }

}