package at.vres.master.mdml.mapping;

import java.util.Map;

/**
 * Class for holding the mapping information from the config JSON for Block names
 */
public class NameMapping implements IMapping {
    private String template;
    private String executeWith;
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

    /**
     * Get the name of the Block this mapping is to be executed with (for correctly timing the generation)
     *
     * @return The name of the Block this mapping is to be executed with
     */
    public String getExecuteWith() {
        return executeWith;
    }

    /**
     * Set the name of the Block this mapping is to be executed with (for correctly timing the generation)
     *
     * @param executeWith Tha name of the Block this mapping is to be executed with
     */
    public void setExecuteWith(String executeWith) {
        this.executeWith = executeWith;
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
