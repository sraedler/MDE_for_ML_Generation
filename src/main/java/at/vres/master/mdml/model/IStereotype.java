package at.vres.master.mdml.model;

import java.util.List;

public interface IStereotype {
    String getName();
    List<IStereotypeAttribute> getAttributes();
    IStereotypeAttribute getAttributeByName(String name);
}
