package assignment6;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test class for CsvReader.
 */
public class CsvReaderTest {

  /**
   * Runs all CsvReader tests.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    testReadReturnsNonEmptyList();
    testFirstRowContainsExpectedHeaders();
    testFirstRowValuesAreReadCorrectly();
    testFieldWithCommaIsHandledCorrectly();
    System.out.println("All CsvReader tests passed.");
  }

  /**
   * Tests that reading the CSV file returns a non-null, non-empty list.
   */
  public static void testReadReturnsNonEmptyList() {
    try {
      List<Map<String, String>> data = CsvReader.read("insurance-company-members.csv");

      check(data != null, "Data should not be null.");
      check(!data.isEmpty(), "Data should not be empty.");

      System.out.println("testReadReturnsNonEmptyList passed.");
    } catch (IOException e) {
      throw new AssertionError("IOException occurred: " + e.getMessage());
    }
  }

  /**
   * Tests that the first row contains all expected headers as keys.
   */
  public static void testFirstRowContainsExpectedHeaders() {
    try {
      List<Map<String, String>> data = CsvReader.read("insurance-company-members.csv");
      Map<String, String> firstRow = data.get(0);

      check(firstRow.containsKey("first_name"), "Missing first_name");
      check(firstRow.containsKey("last_name"), "Missing last_name");
      check(firstRow.containsKey("company_name"), "Missing company_name");
      check(firstRow.containsKey("address"), "Missing address");
      check(firstRow.containsKey("city"), "Missing city");
      check(firstRow.containsKey("county"), "Missing county");
      check(firstRow.containsKey("state"), "Missing state");
      check(firstRow.containsKey("zip"), "Missing zip");
      check(firstRow.containsKey("phone1"), "Missing phone1");
      check(firstRow.containsKey("phone2"), "Missing phone2");
      check(firstRow.containsKey("email"), "Missing email");
      check(firstRow.containsKey("web"), "Missing web");

      System.out.println("testFirstRowContainsExpectedHeaders passed.");
    } catch (IOException e) {
      throw new AssertionError("IOException occurred: " + e.getMessage());
    }
  }

  /**
   * Tests that the first row contains non-empty values for key fields.
   */
  public static void testFirstRowValuesAreReadCorrectly() {
    try {
      List<Map<String, String>> data = CsvReader.read("insurance-company-members.csv");
      Map<String, String> firstRow = data.get(0);

      check(firstRow.get("first_name") != null && !firstRow.get("first_name").isEmpty(),
          "Missing first_name value");
      check(firstRow.get("company_name") != null && !firstRow.get("company_name").isEmpty(),
          "Missing company_name value");
      check(firstRow.get("email") != null && !firstRow.get("email").isEmpty(),
          "Missing email value");

      System.out.println("First row: " + firstRow);
      System.out.println("testFirstRowValuesAreReadCorrectly passed.");
    } catch (IOException e) {
      throw new AssertionError("IOException occurred: " + e.getMessage());
    }
  }

  /**
   * Tests that a field containing a comma is handled correctly.
   */
  public static void testFieldWithCommaIsHandledCorrectly() {
    try {
      List<Map<String, String>> data = CsvReader.read("insurance-company-members.csv");

      boolean foundCommaField = false;

      for (Map<String, String> row : data) {
        String companyName = row.get("company_name");
        String lastName = row.get("last_name");

        if (companyName != null && companyName.contains(",")) {
          foundCommaField = true;
          break;
        }

        if (lastName != null && lastName.contains(",")) {
          foundCommaField = true;
          break;
        }
      }

      check(foundCommaField, "No field containing a comma was handled correctly.");

      System.out.println("testFieldWithCommaIsHandledCorrectly passed.");
    } catch (IOException e) {
      throw new AssertionError("IOException occurred: " + e.getMessage());
    }
  }

  /**
   * Helper method for checking test conditions.
   *
   * @param condition the condition to check
   * @param message   the error message if the condition fails
   */
  private static void check(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }
}
