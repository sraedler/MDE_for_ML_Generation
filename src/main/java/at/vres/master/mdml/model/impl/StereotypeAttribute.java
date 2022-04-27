package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.IStereotypeAttribute;

public class StereotypeAttribute implements IStereotypeAttribute {
    private String name;
    private Object value;

    public StereotypeAttribute() {
    }

    public StereotypeAttribute(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
