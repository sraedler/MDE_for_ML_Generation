package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.MLAttributeInput;

public class MLBoolean extends MLAttributeInput {
    private Boolean value;

    @Override
    public Object getValue() {
        return value;
    }
}
