package at.vres.master.mdml.mapping;

import java.util.List;

public class SimpleJSONInfoHolder {
    private String templateFolder;
    private List<JSONSimpleMapping> mappings;

    public String getTemplateFolder() {
        return templateFolder;
    }

    public void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder;
    }
}
