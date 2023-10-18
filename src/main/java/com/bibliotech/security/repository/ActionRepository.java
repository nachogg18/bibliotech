package com.bibliotech.security.repository;

import com.bibliotech.security.entity.Action;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository {
    Optional<Action> findByName(String name);

    List<Action> findAll();
}
