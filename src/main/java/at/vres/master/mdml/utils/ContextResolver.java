package at.vres.master.mdml.utils;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import java.util.LinkedList;
import java.util.List;

public class ContextResolver {

    public static void appendListElementsAsString(StringBuilder sb, Object variable) {
        if(!sb.isEmpty()) sb.append(",");
        else sb.append("[");
        sb.append("\"").append(resolveVariableFromObject(variable)).append("\"");
    }

    public static String resolveVariableFromObject(Object variable) {
        String result = "";
        if (variable != null) {
            result = variable.toString();
            if (variable instanceof List<?>) {
                if(!((List<?>) variable).isEmpty()) {
                    final StringBuilder sb = new StringBuilder();
                    final List<String> vars = new LinkedList<>();
                    ((List<?>) variable).forEach(s -> appendListElementsAsString(sb, s));
                    result = sb.append("]").toString();
                }
            } else if (variable instanceof Property) {
                result = ((Property) variable).getName();
            } else if (variable instanceof Class) {
                // TODO: will probably have to do more complex resolve here
                result = ((Class) variable).getName();
            }
        }
        return result;
    }
}
