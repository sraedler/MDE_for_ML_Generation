package at.vres.master.mdml.mapping.impl;

import at.vres.master.mdml.mapping.IPrimitiveParameterValue;

public class BooleanParameterValue implements IPrimitiveParameterValue {
    private Boolean paramValue;

    @Override
    public Boolean getParamValue() {
        return paramValue;
    }

    public void setParamValue(Boolean paramValue) {
        this.paramValue = paramValue;
    }
}
