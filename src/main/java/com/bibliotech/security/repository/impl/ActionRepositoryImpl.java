package com.bibliotech.security.repository.impl;

import com.bibliotech.security.entity.Action;
import com.bibliotech.security.repository.ActionRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ActionRepositoryImpl implements ActionRepository {

    @Override
    public Optional<Action> findByName(String name) {
        return Arrays.stream(Action.values()).filter(
                action -> action.name().equals(name)
        ).findAny();
    }

    @Override
    public List<Action> findAll() {
        return Arrays.stream(Action.values()).collect(Collectors.toList());
    }
}