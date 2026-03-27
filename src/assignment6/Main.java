package assignment6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Main entry point for Assignment 6.
 *
 * This program reads customer data from a CSV file and generates
 * emails, letters, or both, based on the provided command line arguments.
 */
public class Main {

    /**
     * Runs the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            Config config = parseArguments(args);
            generateFiles(config);
            System.out.println("Files generated successfully.");
        } catch (IllegalArgumentException exception) {
            System.err.println("Error: " + exception.getMessage());
            printUsage();
        } catch (IOException exception) {
            System.err.println("I/O Error: " + exception.getMessage());
        }
    }

    /**
     * Generates all requested files.
     *
     * @param config parsed command line configuration
     * @throws IOException if reading or writing files fails
     */
    private static void generateFiles(Config config) throws IOException {
        List<Map<String, String>> records = CsvReader.read(config.csvFile);

        String emailTemplateContent = null;
        String letterTemplateContent = null;

        if (config.generateEmail) {
            emailTemplateContent = readFileContent(config.emailTemplate);
        }

        if (config.generateLetter) {
            letterTemplateContent = readFileContent(config.letterTemplate);
        }

        int fileNumber = 1;
        for (Map<String, String> row : records) {
            if (config.generateEmail) {
                String filledEmail = TemplateProcessor.fillTemplate(emailTemplateContent, row);
                FileGenerator.writeToFile(config.outputDir, "email-" + fileNumber + ".txt",
                    filledEmail);
            }

            if (config.generateLetter) {
                String filledLetter = TemplateProcessor.fillTemplate(letterTemplateContent, row);
                FileGenerator.writeToFile(config.outputDir, "letter-" + fileNumber + ".txt",
                    filledLetter);
            }

            fileNumber++;
        }
    }

    /**
     * Reads the full content of a text file.
     *
     * @param filePath path to the file
     * @return file content as a String
     * @throws IOException if the file cannot be read
     */
    private static String readFileContent(String filePath) throws IOException {
        return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
    }

    /**
     * Parses all command line arguments into a Config object.
     *
     * @param args command line arguments
     * @return parsed configuration
     */
    private static Config parseArguments(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No arguments were provided.");
        }

        Config config = new Config();
        int index = 0;

        while (index < args.length) {
            String argument = args[index];

            switch (argument) {
                case "--email":
                    config.generateEmail = true;
                    index++;
                    break;
                case "--letter":
                    config.generateLetter = true;
                    index++;
                    break;
                case "--email-template":
                    config.emailTemplate = readRequiredValue(args, index, argument);
                    index += 2;
                    break;
                case "--letter-template":
                    config.letterTemplate = readRequiredValue(args, index, argument);
                    index += 2;
                    break;
                case "--output-dir":
                    config.outputDir = readRequiredValue(args, index, argument);
                    index += 2;
                    break;
                case "--csv-file":
                    config.csvFile = readRequiredValue(args, index, argument);
                    index += 2;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + argument);
            }
        }

        validateConfig(config);
        return config;
    }

    /**
     * Reads the value following an option that requires one.
     *
     * @param args command line arguments
     * @param index current index
     * @param optionName option name
     * @return the value after the option
     */
    private static String readRequiredValue(String[] args, int index, String optionName) {
        if (index + 1 >= args.length || args[index + 1].startsWith("--")) {
            throw new IllegalArgumentException("Missing value for " + optionName + ".");
        }
        return args[index + 1];
    }

    /**
     * Validates that the parsed configuration is complete and legal.
     *
     * @param config parsed configuration
     */
    private static void validateConfig(Config config) {
        if (!config.generateEmail && !config.generateLetter) {
            throw new IllegalArgumentException(
                "You must provide --email, --letter, or both.");
        }

        if (config.generateEmail && config.emailTemplate == null) {
            throw new IllegalArgumentException(
                "--email provided but no --email-template was given.");
        }

        if (config.generateLetter && config.letterTemplate == null) {
            throw new IllegalArgumentException(
                "--letter provided but no --letter-template was given.");
        }

        if (config.outputDir == null) {
            throw new IllegalArgumentException("--output-dir is required.");
        }

        if (config.csvFile == null) {
            throw new IllegalArgumentException("--csv-file is required.");
        }
    }

    /**
     * Prints usage instructions.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("--email Generate email files.");
        System.out.println("--email-template <path> Path to email template.");
        System.out.println("--letter Generate letter files.");
        System.out.println("--letter-template <path> Path to letter template.");
        System.out.println("--output-dir <path> Output directory.");
        System.out.println("--csv-file <path> CSV input file.");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("--email --email-template email-template.txt "
            + "--output-dir output --csv-file customer.csv");
        System.out.println("--letter --letter-template letter-template.txt "
            + "--output-dir output --csv-file customer.csv");
        System.out.println("--email --email-template email-template.txt "
            + "--letter --letter-template letter-template.txt "
            + "--output-dir output --csv-file customer.csv");
    }

    /**
     * Stores command line options.
     */
    private static class Config {
        private boolean generateEmail;
        private boolean generateLetter;
        private String emailTemplate;
        private String letterTemplate;
        private String outputDir;
        private String csvFile;
    }
}