package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class TransactionLog {
    private TransactionLog() { }

    public static synchronized void append(Path file, String line) {
        try {
            Path parent = file.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.write(file, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write transaction log: " + e.getMessage(), e);
        }
    }
}
