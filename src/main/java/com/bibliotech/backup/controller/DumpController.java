package com.bibliotech.backup.controller;

import com.bibliotech.backup.service.DumpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DumpController {
    private final DumpService dumpService;

    public DumpController(DumpService dumpService) {
        this.dumpService = dumpService;
    }

    @GetMapping("/dumps/{fileName}")
    public String getDump(@PathVariable String fileName) {
        return dumpService.processDump(fileName);
    }

    @GetMapping("/dumps")
    public List<String> listDumps() {
        return dumpService.listDumps();
    }
}
