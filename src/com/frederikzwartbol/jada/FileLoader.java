package com.frederikzwartbol.jada;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * This class loads .Jada files from the given path.
 */
public class FileLoader {
    private static final String[] ALLOWED_FILE_EXTENSIONS = {"jada"};

    private static byte[] getByteStreamFromPath(String path) {
        String[] pathParts = path.split("\\.");
        String extension = pathParts[pathParts.length - 1];
        if (Arrays.asList(ALLOWED_FILE_EXTENSIONS).contains(extension)) {
            try {
                return Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                throw new RuntimeError(null,"Couldn't read file from path.");
            }
        } else {
            throw new RuntimeError(null, "File extension not allowed.");
        }
    }

    public static String generateAbsolutePath (String absoluteModulePath, String relativePath) {
        Path absolutePathModule = Path.of(absoluteModulePath).getParent();
        Path relativePathModule =  Path.of(relativePath);
        Path testRelative = absolutePathModule.resolve(relativePathModule);
        return testRelative.toAbsolutePath().toString();
    }

    public static String readFromPath(String path) {
        return new String(getByteStreamFromPath(path), Charset.defaultCharset());
    }
}
