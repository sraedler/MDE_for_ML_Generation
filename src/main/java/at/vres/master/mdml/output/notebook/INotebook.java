package at.vres.master.mdml.output.notebook;

import java.util.List;

/**
 * Interface for Notebook generation
 */
public interface INotebook {
    /**
     * Returns the name assigned to the notebook (not relevant for generation)
     *
     * @return The name assigned to the notebook
     */
    String getName();

    /**
     * Sets the name for the notebook (not relevant for generation)
     *
     * @param name The name to assign to the notebook
     */
    void setName(String name);

    /**
     * Method for getting all cells that the notebook currently holds
     *
     * @return A List with all cells the notebook currently holds
     */
    List<ICell> getCells();

    /**
     * Method for getting a specific cell based on its ID
     *
     * @param cellId The id of the cell you are looking for
     * @return The cell with the given ID
     */
    ICell getCellById(String cellId);

    /**
     * Returns the execution count counter variable
     *
     * @return The integer value of the current execution count
     */
    Integer getExecutionCount();

    /**
     * Method for getting a specific cell based on the name of a connected element
     *
     * @param elementName The name of the element connected to the desired cell
     * @return The cell with the connected element with the given name
     */
    ICell getCellByElementName(String elementName);

    /**
     * Method for adding a single ICell to the cell list of the notebook
     *
     * @param cellToAdd The cell to add to the cell list of the notebook
     */
    void addCell(ICell cellToAdd);

    /**
     * Get the nbformat Integer value of the notebook
     *
     * @return The nbformat value
     */
    Integer getNbformat();

    /**
     * Get the nbformat_minor Integer value of the notebook
     *
     * @return The nbformat_minor value
     */
    Integer getNbformat_minor();

    /**
     * Set the nbformat Integer value of the notebook
     *
     * @param nbformat The value to set for nbformat
     */
    void setNbformat(Integer nbformat);

    /**
     * Set the nbformat_minor Integer value of the notebook
     *
     * @param nbformat_minor The value to set for nbformat_minor
     */
    void setNbformat_minor(Integer nbformat_minor);

    /**
     * Get the IMetadata object of the notebook
     *
     * @return The IMetadata obhject containing the metadata information of the notebook
     */
    IMetadata getMetadata();

    /**
     * Set the metadata of the notebook
     *
     * @param metadata The IMetadata to set
     */
    void setMetadata(IMetadata metadata);

    /**
     * Add a list of cells to the cell list of the notebook
     *
     * @param cells The list of ICells to add to the notebook
     */
    void addCells(List<ICell> cells);
}
