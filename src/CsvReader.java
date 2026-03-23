import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvReader {

  public static List<Map<String, String>> read(String filePath) throws IOException {
    List<Map<String, String>> records = new ArrayList<>();

    BufferedReader reader = new BufferedReader(new FileReader(filePath));

    String headerLine = reader.readLine();
    if (headerLine == null) {
      reader.close();
      return records;
    }

    List<String> headers = parseCsvLine(headerLine);

    String line;
    while ((line = reader.readLine()) != null) {
      if (line.trim().isEmpty()) continue;

      List<String> values = parseCsvLine(line);
      Map<String, String> row = new LinkedHashMap<>();

      for (int i = 0; i < headers.size() && i < values.size(); i++) {
        row.put(clean(headers.get(i)), clean(values.get(i)));
      }

      records.add(row);
    }

    reader.close();
    return records;
  }

  private static List<String> parseCsvLine(String line) {
    List<String> result = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean inQuotes = false;

    for (char c : line.toCharArray()) {
      if (c == '"') {
        inQuotes = !inQuotes;
      } else if (c == ',' && !inQuotes) {
        result.add(current.toString());
        current.setLength(0);
      } else {
        current.append(c);
      }
    }

    result.add(current.toString());
    return result;
  }

  private static String clean(String s) {
    s = s.trim();
    if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
      s = s.substring(1, s.length() - 1);
    }
    return s.trim();
  }
}
