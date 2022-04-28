package at.vres.master.mdml.model;

import org.eclipse.uml2.uml.Class;

import java.util.List;

public interface IClass {
    List<IProperty> getProperties();

    List<IStereotype> getStereotypes();

    List<IClass> getParts();

    String getName();

    String getQualifiedName();

    Class getUnderlyingClass();
}
