package at.vres.master.mdml.mapping;

import java.util.Map;

public class StereotypeMapping {
    private String template;
    private Map<String, String> properties;
    private Map<String, String> modelCommands;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getModelCommands() {
        return modelCommands;
    }

    public void setModelCommands(Map<String, String> modelCommands) {
        this.modelCommands = modelCommands;
    }
}
