package assignment6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteToFileCreatesFile() throws IOException {
        String outputDir = tempDir.toString();
        String fileName = "test.txt";
        String content = "Hello world";

        FileGenerator.writeToFile(outputDir, fileName, content);

        Path file = tempDir.resolve(fileName);

        assertTrue(Files.exists(file));
    }

    @Test
    void testWriteToFileWritesCorrectContent() throws IOException {
        String outputDir = tempDir.toString();
        String fileName = "content.txt";
        String content = "Generated content";

        FileGenerator.writeToFile(outputDir, fileName, content);

        Path file = tempDir.resolve(fileName);

        String actual = Files.readString(file);

        assertEquals(content, actual);
    }

    @Test
    void testWriteToFileCreatesNestedDirectories() throws IOException {
        String outputDir = tempDir.resolve("emails/output").toString();
        String fileName = "nested.txt";
        String content = "Nested content";

        FileGenerator.writeToFile(outputDir, fileName, content);

        Path file = tempDir.resolve("emails/output").resolve(fileName);

        assertTrue(Files.exists(file));
        assertEquals(content, Files.readString(file));
    }
}