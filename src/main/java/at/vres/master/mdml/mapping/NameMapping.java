package at.vres.master.mdml.mapping;

import java.util.Map;

/**
 * Class for holding the mapping information from the config JSON for Block names
 */
public class NameMapping implements IMapping {
    private String template;
    private Map<String, String> properties;
    private Map<String, String> modelCommands;

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, String> getModelCommands() {
        return modelCommands;
    }

    @Override
    public void setModelCommands(Map<String, String> modelCommands) {
        this.modelCommands = modelCommands;
    }
}
