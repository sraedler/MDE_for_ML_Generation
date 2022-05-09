package at.vres.master.mdml.mapping;

import java.util.Map;

public class MappingV2 {
    private String stereotypeName;
    private String templateName;
    private Map<String, String> properties;

    public String getStereotypeName() {
        return stereotypeName;
    }

    public void setStereotypeName(String stereotypeName) {
        this.stereotypeName = stereotypeName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
