package at.vres.master.mdml.mapping;

import java.util.Map;

/**
 * Interface for mapping objects for JSON mappings
 */
public interface IMapping {

    /**
     * Get the name of the template that is connected to this mapping
     *
     * @return The name of the template connected to this mapping
     */
    String getTemplate();

    /**
     * Set the name of the template that is connected to this mapping
     *
     * @param template The name of the template to set
     */
    void setTemplate(String template);

    /**
     * Get the mapping between original property names and the names they are to be remapped to for merging with the template
     *
     * @return The mapping between original property names and the names to remap to
     */
    Map<String, String> getProperties();

    /**
     * Set the mapping between original property names and the names they are to be remapped to for merging with the template
     *
     * @param properties The mapping between original property names and remapped names to set
     */
    void setProperties(Map<String, String> properties);

    /**
     * Get the mapping between model commands and the names their values are to be remapped to
     *
     * @return The mapping between model commands and the names their values are to be remapped to
     */
    Map<String, String> getModelCommands();

    /**
     * Set the mapping between model commands and the names their values are to be remapped to
     *
     * @param modelCommands The mapping between model commands and their remapped names to set
     */
    void setModelCommands(Map<String, String> modelCommands);
}
