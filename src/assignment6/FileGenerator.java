package assignment6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes generated messages to text files.
 */
public final class FileGenerator {

    private FileGenerator() {
        // Utility class.
    }

    /**
     * Writes content into a file inside the target output directory.
     *
     * @param outputDir the directory where generated files should be stored
     * @param fileName the file name to create
     * @param content the text to write into the file
     * @throws IOException if the file cannot be written
     */
    public static void writeToFile(String outputDir, String fileName, String content)
        throws IOException {
        Path outputDirectory = Path.of(outputDir);
        Files.createDirectories(outputDirectory);

        Path outputFile = outputDirectory.resolve(fileName);
        Files.writeString(outputFile, content, StandardCharsets.UTF_8);
    }
}
