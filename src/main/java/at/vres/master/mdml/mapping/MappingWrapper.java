package at.vres.master.mdml.mapping;

import java.util.HashMap;
import java.util.Map;

public class MappingWrapper {
    public Map<String, StereotypeMapping> stereotypeMappings = new HashMap<>();
    public Map<String, NameMapping> nameMappings = new HashMap<>();

    public Map<String, StereotypeMapping> getStereotypeMappings() {
        return stereotypeMappings;
    }

    public void setStereotypeMappings(Map<String, StereotypeMapping> stereotypeMappings) {
        this.stereotypeMappings = stereotypeMappings;
    }

    public Map<String, NameMapping> getNameMappings() {
        return nameMappings;
    }

    public void setNameMappings(Map<String, NameMapping> nameMappings) {
        this.nameMappings = nameMappings;
    }
}
