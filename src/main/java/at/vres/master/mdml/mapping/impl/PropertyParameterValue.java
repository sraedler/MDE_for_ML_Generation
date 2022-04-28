package at.vres.master.mdml.mapping.impl;

import at.vres.master.mdml.mapping.ITemplateParameterValue;
import org.eclipse.uml2.uml.Property;

public class PropertyParameterValue implements ITemplateParameterValue {
    private Property paramValue;

    @Override
    public Property getParamValue() {
        return paramValue;
    }

    public void setParamValue(Property paramValue) {
        this.paramValue = paramValue;
    }
}
