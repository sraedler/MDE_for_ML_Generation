package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.MLAttributeInput;

public class MLDatetime extends MLAttributeInput {
    private String value;
    private String dateTimeFormat;

    @Override
    public Object getValue() {
        return value;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
