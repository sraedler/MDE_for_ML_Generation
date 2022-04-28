package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.IClass;
import at.vres.master.mdml.model.IProperty;
import at.vres.master.mdml.model.IStereotype;
import org.eclipse.uml2.uml.Class;

import java.util.List;

public class MLClass implements IClass{
    private String name;
    private String qualifiedName;
    private Class underlyingClass;
    private List<IProperty> properties;
    private List<IStereotype> stereotypes;
    private List<IClass> parts;

    public MLClass(String name, String qualifiedName, Class underlyingClass, List<IProperty> properties, List<IStereotype> stereotypes, List<IClass> parts) {
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.underlyingClass = underlyingClass;
        this.properties = properties;
        this.stereotypes = stereotypes;
        this.parts = parts;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public Class getUnderlyingClass() {
        return underlyingClass;
    }

    public void setUnderlyingClass(Class underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    public List<IProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<IProperty> properties) {
        this.properties = properties;
    }

    @Override
    public List<IStereotype> getStereotypes() {
        return stereotypes;
    }

    public void setStereotypes(List<IStereotype> stereotypes) {
        this.stereotypes = stereotypes;
    }

    @Override
    public List<IClass> getParts() {
        return parts;
    }

    public void setParts(List<IClass> parts) {
        this.parts = parts;
    }
}
