package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.MLAttributeInput;
import at.vres.master.mdml.model.enums.NumberRange;

public class MLInteger extends MLAttributeInput {
    private Integer value;
    private NumberRange valueRange;

    @Override
    public Object getValue() {
        return value;
    }

    public NumberRange getValueRange() {
        return valueRange;
    }

    public void setValueRange(NumberRange valueRange) {
        this.valueRange = valueRange;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
