package com.bibliotech.security.controller;

import com.bibliotech.security.entity.Resource;
import com.bibliotech.security.service.ResourceService;
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

  @Autowired private final ResourceService resourceService;

    @GetMapping("")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PRIVILEGE')")
    public List<Resource> findAll() {
         return resourceService.getAllResources();
    }

  
}
