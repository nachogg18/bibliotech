package com.bibliotech.security.controller;

import com.bibliotech.security.entity.Action;
import com.bibliotech.security.service.ActionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/actions")
@RequiredArgsConstructor
public class ActionController {
  private static final Logger logger = LoggerFactory.getLogger(ActionController.class);

  @Autowired private final ActionService actionService;

    @GetMapping("")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PRIVILEGE')")
    public List<Action> findAll() {
         return actionService.getAllActions();
    }

  
}
