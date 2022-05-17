package at.vres.master.mdml.formatters;

public class EmptyLineFormatter {

    public static String format(String input) {
        final StringBuilder sb = new StringBuilder();
        input.lines().forEach(line -> {
            if (!line.isEmpty()) sb.append(line).append("\n");
        });
        return sb.toString();
    }
}
