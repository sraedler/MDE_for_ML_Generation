package at.vres.master.mdml.model;

import org.eclipse.uml2.uml.Property;

import java.util.List;

public interface IProperty {
    List<IStereotype> getStereotypes();

    String getName();

    String getQualifiedName();

    Property getUnderlyingProperty();

    Object getValue();
}
