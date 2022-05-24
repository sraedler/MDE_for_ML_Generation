package at.vres.master.mdml.output.notebook;

import java.util.List;
import java.util.Map;

/**
 * Interface for the cells of a notebook
 */
public interface ICell {

    /**
     * Get the String representation of the notebook type
     *
     * @return The type of the notebook as a String
     */
    String getCell_type();

    /**
     * Get the list of strings that represent the source (meaning the actual content) of the cell
     *
     * @return A String-list representing the content of the cell
     */
    List<String> getSource();

    /**
     * Get a map representing the metadata of the cell
     *
     * @return A Map<String, Object> containing the metadata of the cell
     */
    Map<String, Object> getMetadata();

    /**
     * Get the cell's unique ID
     *
     * @return The unique ID of the cell as a String
     */
    String getId();

    /**
     * Get a list of the names of all elements connected to the cell
     *
     * @return A List<String> containing the names of all elements connected to the cell
     */
    List<String> getConnectedElementNames();

    /**
     * Add a String to the list containing the source (content) of the cell
     *
     * @param toAdd The String to add to the list
     */
    void addToSource(String toAdd);

    /**
     * Add a List<String> to the list containing the source (content) of the cell
     *
     * @param allToAdd The List<String> to add to the source
     */
    void addAllToSource(List<String> allToAdd);

    /**
     * Set the cell type of the cell
     *
     * @param cell_type The new cell type of the cell
     */
    void setCell_type(CellCategory cell_type);

    /**
     * Add the name of a connected element to the list of connected element names
     *
     * @param name The name of the connected element to add to the list
     */
    void addConnectedElementName(String name);

    /**
     * Add a list of names of connected elements to the list of connected element names
     *
     * @param names The List<String> of names to add to the connected element names list
     */
    void addConnectedElementNames(List<String> names);

    /**
     * Get the names of all variables in this cell
     *
     * @return A List<String> of all variable names of this cell
     */
    List<String> getVariables();

    /**
     * Add the name of a variable to the list of variables names for this cell
     *
     * @param variable The name of the variable to add
     */
    void addVariable(String variable);

    /**
     * Add a list of variable names to the list of variable names for this cell
     *
     * @param variables The List<String> of variable names to add
     */
    void addVariables(List<String> variables);
}
