package at.vres.master.mdml.formatters;

/**
 * Class for removing empty lines from generated code
 */
public class EmptyLineFormatter {

    /**
     * Format an input String in such a way that all empty lines are removed
     *
     * @param input The String to format
     * @return A new String where all empty lines have been removed
     */
    public static String format(String input) {
        final StringBuilder sb = new StringBuilder();
        input.lines().forEach(line -> {
            if (!line.isEmpty()) sb.append(line).append("\n");
        });
        return sb.toString();
    }
}
