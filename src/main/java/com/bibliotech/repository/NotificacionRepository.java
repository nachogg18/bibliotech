package com.bibliotech.repository;

import com.bibliotech.entity.Notificacion;
import com.bibliotech.security.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends BaseRepository<Notificacion, Long>{
    List<Notificacion> findByUserID(Long userID);
    List<Notificacion> findByUserIDAndLeidoIsFalse(Long userID);
}
