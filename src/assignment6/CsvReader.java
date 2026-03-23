package assignment6;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads customer data from a CSV file.
 *
 * <p>The CSV format used in this assignment stores every value in double quotes,
 * and some values may contain commas inside quoted text.</p>
 */
public final class CsvReader {

    private CsvReader() {
        // Utility class.
    }

    /**
     * Reads a CSV file and converts each row into a map from header to value.
     *
     * @param filePath the path to the CSV file
     * @return a list of records, one map per row
     * @throws IOException if the file cannot be read
     */
    public static List<Map<String, String>> read(String filePath) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
            Path.of(filePath), StandardCharsets.UTF_8)) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                return records;
            }

            List<String> headers = parseCsvLine(headerLine);
            for (int i = 0; i < headers.size(); i++) {
                headers.set(i, clean(headers.get(i)));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> values = parseCsvLine(line);
                Map<String, String> row = new LinkedHashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    String value = i < values.size() ? clean(values.get(i)) : "";
                    row.put(headers.get(i), value);
                }

                records.add(row);
            }
        }

        return records;
    }

    /**
     * Parses one CSV line while respecting quoted values and escaped quotes.
     *
     * @param line one line from the CSV file
     * @return the parsed values from the line
     */
    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (currentChar == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(currentChar);
            }
        }

        result.add(current.toString());
        return result;
    }

    /**
     * Trims whitespace and removes surrounding quotes when present.
     *
     * @param text the raw CSV token
     * @return the cleaned token
     */
    private static String clean(String text) {
        String cleaned = text.trim();
        if (cleaned.startsWith("\"") && cleaned.endsWith("\"") && cleaned.length() >= 2) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned.trim();
    }
}
