package at.vres.master.mdml.model;

import org.eclipse.uml2.uml.Property;

public abstract class MLAttributeInput implements ML {
    private String mappedName;
    private Property underlyingProperty;

    public String getMappedName() {
        return mappedName;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    abstract public Object getValue();

    public Property getUnderlyingProperty() {
        return underlyingProperty;
    }

    public void setUnderlyingProperty(Property underlyingProperty) {
        this.underlyingProperty = underlyingProperty;
    }
}
