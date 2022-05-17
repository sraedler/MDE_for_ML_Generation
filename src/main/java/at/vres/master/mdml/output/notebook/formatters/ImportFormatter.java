package at.vres.master.mdml.output.notebook.formatters;

import java.util.LinkedList;
import java.util.List;

public class ImportFormatter {
    private static final String KEYWORD_IMPORT = "import";

    public static String format(String input) {
        final StringBuilder importLines = new StringBuilder();
        final StringBuilder codeLines = new StringBuilder();
        input.lines().forEach(line -> {
            if (line.contains(KEYWORD_IMPORT)) {
                importLines.append(line).append("\n");
            } else {
                codeLines.append(line).append("\n");
            }
        });
        return importLines.append("\n").append(codeLines).toString();
    }
}
