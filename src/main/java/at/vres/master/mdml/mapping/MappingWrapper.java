package at.vres.master.mdml.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingWrapper {
    private List<String> blockedStereotypes;
    private List<String> blockedNames;
    public Map<String, StereotypeMapping> stereotypeMappings = new HashMap<>();
    public Map<String, NameMapping> nameMappings = new HashMap<>();
    public Map<String, String> constants = new HashMap<>();

    public List<String> getBlockedStereotypes() {
        return blockedStereotypes;
    }

    public void setBlockedStereotypes(List<String> blockedStereotypes) {
        this.blockedStereotypes = blockedStereotypes;
    }

    public List<String> getBlockedNames() {
        return blockedNames;
    }

    public void setBlockedNames(List<String> blockedNames) {
        this.blockedNames = blockedNames;
    }

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

    public Map<String, String> getConstants() {
        return constants;
    }

    public void setConstants(Map<String, String> constants) {
        this.constants = constants;
    }
}
