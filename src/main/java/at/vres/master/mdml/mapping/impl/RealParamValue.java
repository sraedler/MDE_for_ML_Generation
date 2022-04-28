package at.vres.master.mdml.mapping.impl;

import at.vres.master.mdml.mapping.IPrimitiveParameterValue;

public class RealParamValue implements IPrimitiveParameterValue {
    private Double paramValue;

    @Override
    public Double getParamValue() {
        return paramValue;
    }

    public void setParamValue(Double paramValue) {
        this.paramValue = paramValue;
    }
}
