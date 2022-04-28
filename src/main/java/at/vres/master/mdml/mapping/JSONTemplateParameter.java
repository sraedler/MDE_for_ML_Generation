package at.vres.master.mdml.mapping;

public class JSONTemplateParameter implements ITemplateParameter{
    String name;
    String valueString;
    ITemplateParameterValue parameterValue;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public ITemplateParameterValue getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(ITemplateParameterValue parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public ITemplateParameterValue getValue() {
        return getParameterValue();
    }
}
