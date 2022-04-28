package at.vres.master.mdml.decomposition;

import at.vres.master.mdml.model.IClass;
import at.vres.master.mdml.model.IProperty;
import at.vres.master.mdml.model.IStereotype;
import at.vres.master.mdml.model.IStereotypeAttribute;
import at.vres.master.mdml.model.impl.MLClass;
import at.vres.master.mdml.model.impl.MLProperty;
import at.vres.master.mdml.model.impl.MLStereotype;
import at.vres.master.mdml.model.impl.StereotypeAttribute;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ElementConverter {
    private static final Map<NamedElement, List<NamedElement>> connections = new HashMap<>();

    public static IClass classConversion(Class clazz) {
        final List<IStereotype> stereotypes  = new LinkedList<>();
        final List<IProperty> properties = new LinkedList<>();
        final List<IClass> parts = new LinkedList<>();
        clazz.getAppliedStereotypes().forEach(s -> stereotypes.add(stereotypeConversion(s, clazz)));
        clazz.getAllAttributes().forEach(p -> properties.add(propertyConversion(p)));
        return new MLClass(clazz.getName(), clazz.getQualifiedName(), clazz, properties, stereotypes, parts);
    }

    public static IProperty propertyConversion(Property property) {
        List<IStereotype> stereotypes = new LinkedList<>();
        property.getAppliedStereotypes().forEach(st -> {
            stereotypes.add(stereotypeConversion(st, property));
        });
        // TODO make value element
        return new MLProperty(property.getName(), property.getQualifiedName(), stereotypes, property, property.getDefault());
    }

    public static IStereotype stereotypeConversion(Stereotype stereotype, Element element) {
        MLStereotype mls = new MLStereotype();
        mls.setName(stereotype.getName());
        final List<IStereotypeAttribute> atts = new LinkedList<>();
        stereotype.getAllAttributes().forEach(att -> {
            Object value = element.getValue(stereotype, att.getName());
            atts.add(stereotypeAttributeConversion(value, att.getName()));
        });
        mls.setAttributes(atts);
        return mls;
    }

    public static IStereotypeAttribute stereotypeAttributeConversion(Object value, String attributeName) {
        return new StereotypeAttribute(attributeName, value);
    }


}
