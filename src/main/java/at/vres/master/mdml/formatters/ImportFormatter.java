package at.vres.master.mdml.formatters;

import java.util.LinkedHashSet;
import java.util.Set;

public class ImportFormatter {
    private static final String KEYWORD_IMPORT = "import";

    public static String format(String input) {
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
