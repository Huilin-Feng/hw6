public class Main {
  public static void main(String[] args) {
    try {
      var data = CsvReader.read("insurance-company-members.csv");

      for (var row : data) {
        System.out.println(row);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
