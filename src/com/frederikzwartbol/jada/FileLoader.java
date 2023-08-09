package com.frederikzwartbol.jada;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileLoader {
    private static final String[] ALLOWED_FILE_EXTENSIONS = {"jada"};

    private byte[] getByteStreamFromPath(String path) {
        String[] pathParts = path.split("\\.");
        String extension = pathParts[pathParts.length - 1];
        if (Arrays.asList(ALLOWED_FILE_EXTENSIONS).contains(extension)) {
            try {
                return Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeError(null, "File extension not allowed.");
        }
    }

    public String readFromPath(String path) {
        return new String(getByteStreamFromPath(path), Charset.defaultCharset());
    }
}
