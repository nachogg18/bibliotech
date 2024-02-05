package com.bibliotech.backup.repository;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DumpRepository {
    private static final String DUMP_DIRECTORY = "/backup";

    public String readDump(String fileName) throws IOException {
        Path filePath = Paths.get(DUMP_DIRECTORY, fileName);
        return new String(Files.readAllBytes(filePath));
    }

    public List<String> listDumps() {
        File dumpDir = new File(DUMP_DIRECTORY);
        return Arrays.stream(dumpDir.listFiles())
                .map(File::getName)
                .collect(Collectors.toList());
    }
}
