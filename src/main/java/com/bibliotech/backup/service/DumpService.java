package com.bibliotech.backup.service;

import com.bibliotech.backup.repository.DumpRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DumpService {
    private final DumpRepository dumpRepository;

    public DumpService(DumpRepository dumpRepository) {
        this.dumpRepository = dumpRepository;
    }

    public String processDump(String fileName) {
        try {
            String dump = dumpRepository.readDump(fileName);
            // Aquí puedes procesar el volcado como necesites
            return dump;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el volcado", e);
        }
    }

    public List<String> listDumps() {
        return dumpRepository.listDumps();
    }
}
