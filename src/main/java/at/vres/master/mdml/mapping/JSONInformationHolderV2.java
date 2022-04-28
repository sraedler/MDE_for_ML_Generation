package at.vres.master.mdml.mapping;

import at.vres.master.mdml.model.ML;

import java.util.List;

public class JSONInformationHolderV2 {
    private String templateFolder;
    private List<IMapping> mappings;

    public String getTemplateFolder() {
        return templateFolder;
    }

    public void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder;
    }

    public List<IMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<IMapping> mappings) {
        this.mappings = mappings;
    }
}
