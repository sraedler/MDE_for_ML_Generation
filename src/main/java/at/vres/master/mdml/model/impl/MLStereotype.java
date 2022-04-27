package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.IStereotype;
import at.vres.master.mdml.model.IStereotypeAttribute;

import java.util.LinkedList;
import java.util.List;

public class MLStereotype implements IStereotype {
    private String name;
    private List<IStereotypeAttribute> attributes = new LinkedList<>();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<IStereotypeAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public IStereotypeAttribute getAttributeByName(String name) {
        return attributes.stream().filter(att -> att.getName().equals(name)).findFirst().orElse(null);
    }

    public Boolean addAttribute(IStereotypeAttribute attribute) {
        return attributes.add(attribute);
    }

    public void setAttributes(List<IStereotypeAttribute> attributes) {
        this.attributes = attributes;
    }
}
