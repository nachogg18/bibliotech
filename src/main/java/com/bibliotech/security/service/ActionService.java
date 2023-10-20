package com.bibliotech.security.service;


import com.bibliotech.security.entity.Action;
import java.util.List;
import java.util.Optional;

public interface ActionService {
    List<Action> getAllActions();
    Optional<Action> getActionByName(String name);
}