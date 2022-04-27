package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.MLAttributeInput;
import at.vres.master.mdml.model.enums.NumberRange;

public class MLFloat extends MLAttributeInput {
    private Double value;
    private NumberRange valueRange;

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public NumberRange getValueRange() {
        return valueRange;
    }

    public void setValueRange(NumberRange valueRange) {
        this.valueRange = valueRange;
    }
}
