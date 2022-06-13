package at.vres.master.mdml.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object for deserializing the configuration JSON file
 */
public class MappingWrapper {
    private Boolean trimEmptyLines;
    private List<String> blockedStereotypes;
    private List<String> blockedNames;
    public Map<String, StereotypeMapping> stereotypeMappings = new HashMap<>();
    public Map<String, NameMapping> nameMappings = new HashMap<>();
    public Map<String, String> constants = new HashMap<>();

    /**
     * Getter for trimEmptyLines attribute (whether empty line should be trimmed or not)
     *
     * @return The value of the trimEmptyLines attribute
     */
    public Boolean getTrimEmptyLines() {
        return trimEmptyLines;
    }

    /**
     * Setter for trimEmptyLines attribute (whether empty line should be trimmed or not)
     *
     * @param trimEmptyLines The value of the trimEmptyLines attribute to set
     */
    public void setTrimEmptyLines(Boolean trimEmptyLines) {
        this.trimEmptyLines = trimEmptyLines;
    }

    /**
     * Get the names of the stereotypes for which no code is to be generated
     *
     * @return The list of stereotype names for which no generation shall take place
     */
    public List<String> getBlockedStereotypes() {
        return blockedStereotypes;
    }

    /**
     * Set the names of the stereotypes for which no code is to be generated
     *
     * @param blockedStereotypes A list of names of stereotypes to not generate code for
     */
    public void setBlockedStereotypes(List<String> blockedStereotypes) {
        this.blockedStereotypes = blockedStereotypes;
    }

    /**
     * Get the names of the blocks for which no code is to be generated
     *
     * @return The list of block names for which no generation shall take place
     */
    public List<String> getBlockedNames() {
        return blockedNames;
    }

    /**
     * Set the names of the blocks for which no code is to be generated
     *
     * @param blockedNames A list of names of blocks to not generate code for
     */
    public void setBlockedNames(List<String> blockedNames) {
        this.blockedNames = blockedNames;
    }

    /**
     * Get the stereotype mappings, where the key is the name of the stereotype and the value is a Stereotype mapping
     *
     * @return The Map of stereotype names to their JSON mappings
     */
    public Map<String, StereotypeMapping> getStereotypeMappings() {
        return stereotypeMappings;
    }

    /**
     * Set the stereotype mappings, where the key is the name of the stereotype and the value is a Stereotype mapping
     *
     * @param stereotypeMappings The Map of stereotype names to their JSON mappings to set
     */
    public void setStereotypeMappings(Map<String, StereotypeMapping> stereotypeMappings) {
        this.stereotypeMappings = stereotypeMappings;
    }

    /**
     * Get the name mappings, where the key is the name of the block and the value is a Name mapping
     *
     * @return The Map of block names to their JSON mappings
     */
    public Map<String, NameMapping> getNameMappings() {
        return nameMappings;
    }

    /**
     * Set the name mappings, where the key is the name of the block and the value is a Name mapping
     *
     * @param nameMappings The Map of block names to their JSON mappings to set
     */
    public void setNameMappings(Map<String, NameMapping> nameMappings) {
        this.nameMappings = nameMappings;
    }

    /**
     * Get the Map of names of constants and their values
     *
     * @return The Map of constant names and their values
     */
    public Map<String, String> getConstants() {
        return constants;
    }

    /**
     * Set the Map of names of constants and their values
     *
     * @param constants The Map of constant names and their values to set
     */
    public void setConstants(Map<String, String> constants) {
        this.constants = constants;
    }
}
