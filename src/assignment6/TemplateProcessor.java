package assignment6;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replaces placeholders in a template with values from one CSV row.
 *
 * <p>Placeholders use the form [[header_name]].</p>
 */
public final class TemplateProcessor {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\[\\[(.+?)\\]\\]");

    private TemplateProcessor() {
        // Utility class.
    }

    /**
     * Fills a template using the data from one customer record.
     *
     * @param template the template text
     * @param rowData one CSV row as a map from header to value
     * @return the completed text with placeholders replaced
     */
    public static String fillTemplate(String template, Map<String, String> rowData) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String replacement = rowData.getOrDefault(key, "");
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
