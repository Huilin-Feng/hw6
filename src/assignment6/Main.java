package assignment6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Entry point for the insurance company communication generator.
 */
public final class Main {

    private Main() {
        // Prevent instantiation.
    }

    /**
     * Runs the program using command line arguments.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            Config config = parseArguments(args);
            generateFiles(config);
            System.out.println("Files generated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println();
            printUsage();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateFiles(Config config) throws IOException {
        List<Map<String, String>> data = CsvReader.read(config.csvFile);

        String emailTemplate = null;
        String letterTemplate = null;

        if (config.generateEmail) {
            emailTemplate = Files.readString(Path.of(config.emailTemplate), StandardCharsets.UTF_8);
        }
        if (config.generateLetter) {
            letterTemplate = Files.readString(Path.of(config.letterTemplate), StandardCharsets.UTF_8);
        }

        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);
            int fileNumber = i + 1;

            if (config.generateEmail) {
                String filledEmail = TemplateProcessor.fillTemplate(emailTemplate, row);
                FileGenerator.writeToFile(config.outputDir,
                    "email-" + fileNumber + ".txt", filledEmail);
            }

            if (config.generateLetter) {
                String filledLetter = TemplateProcessor.fillTemplate(letterTemplate, row);
                FileGenerator.writeToFile(config.outputDir,
                    "letter-" + fileNumber + ".txt", filledLetter);
            }
        }
    }

    private static Config parseArguments(String[] args) {
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

    private static String readRequiredValue(String[] args, int index, String optionName) {
        if (index + 1 >= args.length || args[index + 1].startsWith("--")) {
            throw new IllegalArgumentException("Missing value for " + optionName + ".");
        }
        return args[index + 1];
    }

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

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("--email Generate email messages. If this option is provided,");
        System.out.println("        then --email-template must also be provided.");
        System.out.println("--email-template <path/to/file> A filename for the email template.");
        System.out.println("--letter Generate letters. If this option is provided,");
        System.out.println("         then --letter-template must also be provided.");
        System.out.println("--letter-template <path/to/file> A filename for the letter template.");
        System.out.println("--output-dir <path/to/folder> The folder to store all generated files.");
        System.out.println("--csv-file <path/to/file> The CSV file to process.");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("--email --email-template email-template.txt "
            + "--output-dir emails --csv-file customer.csv");
        System.out.println("--letter --letter-template letter-template.txt "
            + "--output-dir letters --csv-file customer.csv");
        System.out.println("--email --email-template email-template.txt --letter "
            + "--letter-template letter-template.txt --output-dir output "
            + "--csv-file customer.csv");
    }

    /**
     * Stores all parsed command line options.
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
