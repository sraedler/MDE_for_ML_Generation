package at.vres.master.mdml.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

public class JSONInformationHolder {
    private String template;
    private Map<String, Object> parameters = new LinkedHashMap<>();

    public JSONInformationHolder() {
    }

    public JSONInformationHolder(String template, Map<String, Object> parameters) {
        this.template = template;
        this.parameters = parameters;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void putParameter(String paramName, Object paramValue) {
        parameters.putIfAbsent(paramName, paramValue);
    }

    public Object getParameterValue(String paramName) {
        return parameters.get(paramName);
    }
}
