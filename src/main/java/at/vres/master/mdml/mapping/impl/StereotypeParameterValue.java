package at.vres.master.mdml.mapping.impl;

import at.vres.master.mdml.mapping.ITemplateParameterValue;
import at.vres.master.mdml.model.IStereotype;

public class StereotypeParameterValue implements ITemplateParameterValue {
    private IStereotype paramValue;

    @Override
    public IStereotype getParamValue() {
        return paramValue;
    }

    public void setParamValue(IStereotype paramValue) {
        this.paramValue = paramValue;
    }
}
