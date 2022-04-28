package at.vres.master.mdml.mapping.impl;

import at.vres.master.mdml.mapping.ITemplateParameterValue;
import org.eclipse.uml2.uml.Class;

public class PartParameterValue implements ITemplateParameterValue {
    private Class paramValue;

    @Override
    public Class getParamValue() {
        return paramValue;
    }

    public void setParamValue(Class paramValue) {
        this.paramValue = paramValue;
    }
}
