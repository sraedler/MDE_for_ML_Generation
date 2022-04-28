package at.vres.master.mdml.mapping;

import at.vres.master.mdml.model.ML;

import java.util.Map;

public class JSONSimpleMapping {
    private String templateName;
    private String mlConnection;
    private Map<String, String> parameters;

    public JSONSimpleMapping(String templateName, String mlConnection, Map<String, String> parameters) {
        this.templateName = templateName;
        this.mlConnection = mlConnection;
        this.parameters = parameters;
    }
}
