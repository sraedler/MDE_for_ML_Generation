package at.vres.master.mdml.mapping;

import at.vres.master.mdml.model.ML;

import java.util.Map;

public class JSONSimpleMapping {
    private String template;
    private String mlConnection;
    private Map<String, String> parameters;

    public JSONSimpleMapping(String template, String mlConnection, Map<String, String> parameters) {
        this.template = template;
        this.mlConnection = mlConnection;
        this.parameters = parameters;
    }

    public JSONSimpleMapping() {
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getMlConnection() {
        return mlConnection;
    }

    public void setMlConnection(String mlConnection) {
        this.mlConnection = mlConnection;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
