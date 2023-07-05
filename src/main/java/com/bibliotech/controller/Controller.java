package com.bibliotech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/insecured")
    public String insecured() {
        return "insecured";
    }

    @GetMapping("/secured")
    public String secured() {
        return "secured";
    }
}
