package at.vres.master.mdml.formatters;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class for formatting the import sections of the generated code
 */
public class ImportFormatter {
    private static final String KEYWORD_IMPORT = "import";

    /**
     * Formats a string in such a way that all imports are at the top of the String and removes duplicate imports
     *
     * @param input The String to format
     * @return A new String where all imports are at the top and all duplicate imports are removed
     */
    public static String format(final String input) {
        final Set<String> importLines = new LinkedHashSet<>();
        final StringBuilder codeLines = new StringBuilder();
        input.lines().forEach(line -> {
            if (line.contains(KEYWORD_IMPORT)) {
                importLines.add(line);
            } else {
                codeLines.append(line).append("\n");
            }
        });
        return String.join("\n", importLines) + codeLines;
    }
}
