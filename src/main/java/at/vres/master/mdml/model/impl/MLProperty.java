package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.IProperty;
import at.vres.master.mdml.model.IStereotype;
import org.eclipse.uml2.uml.Property;

import java.util.List;

public class MLProperty implements IProperty {
    private String name;
    private String qualifiedName;
    private List<IStereotype> stereotypes;
    private Property underlyingProperty;
    private Object value;

    public MLProperty() {
    }

    public MLProperty(String name, String qualifiedName, List<IStereotype> stereotypes, Property underlyingProperty, Object value) {
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.stereotypes = stereotypes;
        this.underlyingProperty = underlyingProperty;
        this.value = value;
    }

    public List<IStereotype> setStereotypes() {
        return null;
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
    public List<IStereotype> getStereotypes() {
        return stereotypes;
    }

    public void setStereotypes(List<IStereotype> stereotypes) {
        this.stereotypes = stereotypes;
    }

    @Override
    public Property getUnderlyingProperty() {
        return underlyingProperty;
    }

    public void setUnderlyingProperty(Property underlyingProperty) {
        this.underlyingProperty = underlyingProperty;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
