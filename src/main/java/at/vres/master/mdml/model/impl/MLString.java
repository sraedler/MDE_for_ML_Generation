package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.MLAttributeInput;
import at.vres.master.mdml.model.enums.StringType;

public class MLString extends MLAttributeInput {
    private String value;
    private StringType valueRange;

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringType getValueRange() {
        return valueRange;
    }

    public void setValueRange(StringType valueRange) {
        this.valueRange = valueRange;
    }
}
