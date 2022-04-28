package at.vres.master.mdml.mapping.impl;

import at.vres.master.mdml.mapping.IPrimitiveParameterValue;

public class StringParameterValue implements IPrimitiveParameterValue {
    private String paramValue;

    @Override
    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
