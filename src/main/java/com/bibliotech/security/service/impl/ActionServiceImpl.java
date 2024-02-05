package com.bibliotech.security.service.impl;

import com.bibliotech.security.entity.Action;
import com.bibliotech.security.repository.ActionRepository;
import com.bibliotech.security.service.ActionService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActionServiceImpl implements ActionService {

    @Autowired
    private ActionRepository actionRepository;


    @Override
    public List<Action> getAllActions() {
        return actionRepository.findAll();
    }

    @Override
    public Optional<Action> getActionByName(String name) {
        return actionRepository.findByName(name);
    }
}
