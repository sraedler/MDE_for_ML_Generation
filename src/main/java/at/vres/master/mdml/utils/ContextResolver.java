package at.vres.master.mdml.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

import java.util.List;

public class ContextResolver {

    public static void appendListElementsAsString(StringBuilder sb, Object variable) {
        if (!sb.isEmpty()) sb.append(",");
        else sb.append("[");
        sb.append("\"").append(resolveVariableFromObject(variable)).append("\"");
    }

    public static String resolveVariableFromObject(Object variable) {
        String result = "";
        if (variable != null) {
            result = variable.toString();
            if (variable instanceof List<?>) {
                if (!((List<?>) variable).isEmpty()) {
                    final StringBuilder sb = new StringBuilder();
                    ((List<?>) variable).forEach(s -> appendListElementsAsString(sb, s));
                    result = sb.append("]").toString();
                }
            } else if (variable instanceof Property) {
                result = ((Property) variable).getName();
            } else if (variable instanceof Class) {
                result = resolveClassVariable((Class) variable);
            }
        }
        return result;
    }

    public static Boolean isStereotypeApplied(Class clazz, String stereoName) {
        return clazz.getAppliedStereotypes().stream().anyMatch(s -> s.getName().equals(stereoName));
    }

    public static String resolveClassVariable(Class clazz) {
        String result = "";
        if (clazz != null) {
            System.out.println("clazz = " + clazz.getName() + " -> Stereo CSV applied: " + isStereotypeApplied(clazz, "CSV"));
            if (isStereotypeApplied(clazz, "CSV")) {
                Stereotype stereo = clazz.getAppliedStereotypes().stream().filter(st -> st.getName().equals("CSV")).findFirst().orElse(null);
                Object value = clazz.getValue(stereo, "VariableName");
                result = value.toString();
            }
        }
        return result;

    }
}
