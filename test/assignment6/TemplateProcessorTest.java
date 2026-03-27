package assignment6;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TemplateProcessorTest {

    @Test
    void testFillTemplateReplacesSinglePlaceholder() {
        Map<String, String> rowData = new HashMap<>();
        rowData.put("first_name", "Art");

        String template = "Hello [[first_name]]!";
        String result = TemplateProcessor.fillTemplate(template, rowData);

        assertEquals("Hello Art!", result);
    }

    @Test
    void testFillTemplateReplacesMultiplePlaceholders() {
        Map<String, String> rowData = new HashMap<>();
        rowData.put("first_name", "Art");
        rowData.put("last_name", "Venere");
        rowData.put("email", "art@venere.org");

        String template = "Name: [[first_name]] [[last_name]], Email: [[email]]";
        String result = TemplateProcessor.fillTemplate(template, rowData);

        assertEquals("Name: Art Venere, Email: art@venere.org", result);
    }

    @Test
    void testFillTemplateLeavesNormalTextUnchanged() {
        Map<String, String> rowData = new HashMap<>();
        rowData.put("first_name", "Art");

        String template = "No placeholders here.";
        String result = TemplateProcessor.fillTemplate(template, rowData);

        assertEquals("No placeholders here.", result);
    }

    @Test
    void testFillTemplateMissingPlaceholderUsesEmptyString() {
        Map<String, String> rowData = new HashMap<>();
        rowData.put("first_name", "Art");

        String template = "Hello [[middle_name]]!";
        String result = TemplateProcessor.fillTemplate(template, rowData);

        assertEquals("Hello !", result);
    }

    @Test
    void testFillTemplateTrimsSpacesInsidePlaceholder() {
        Map<String, String> rowData = new HashMap<>();
        rowData.put("first_name", "Art");

        String template = "Hello [[ first_name ]]!";
        String result = TemplateProcessor.fillTemplate(template, rowData);

        assertEquals("Hello Art!", result);
    }
}