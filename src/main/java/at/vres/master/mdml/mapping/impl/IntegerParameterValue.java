package at.vres.master.mdml.mapping.impl;

import at.vres.master.mdml.mapping.IPrimitiveParameterValue;

public class IntegerParameterValue implements IPrimitiveParameterValue {
    private Integer paramValue;

    @Override
    public Integer getParamValue() {
        return paramValue;
    }

    public void setParamValue(Integer paramValue) {
        this.paramValue = paramValue;
    }
}
