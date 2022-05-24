package at.vres.master.mdml.output.generation;

import at.vres.master.mdml.output.notebook.CellCategory;
import at.vres.master.mdml.output.notebook.ICell;
import at.vres.master.mdml.output.notebook.IMetadata;
import at.vres.master.mdml.output.notebook.INotebook;
import at.vres.master.mdml.output.notebook.impl.PythonCell;
import at.vres.master.mdml.output.notebook.impl.PythonMetadata;
import at.vres.master.mdml.output.notebook.impl.PythonNotebook;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class for helping with the generation of notebooks (currently Python notebooks only)
 */
public class NotebookGenerator {

    /**
     * Method for merging two ICells into each other. The second cell will be merged into the first one.
     *
     * @param first  The first ICell to merge
     * @param second The second ICell to merge
     * @return The merged ICell (is the first ICell with the information from the second ICell merged into it)
     */
    public static ICell mergeCellIntoCell(ICell first, ICell second) {
        if (first.getCell_type().equals(second.getCell_type())) {  // only merge cells of the same type
            first.addConnectedElementNames(second.getConnectedElementNames());
            first.addToSource("\n");
            first.addAllToSource(second.getSource());
            first.addVariables(second.getVariables());
        }
        return first;
    }

    /**
     * Method to write an INotebook to a file as a JSON
     *
     * @param path     The path to the write the notebook to (including name and file ending)
     * @param notebook The notebook to write to the file
     */
    public static void generateTo(String path, INotebook notebook) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(path), notebook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for creating a Notebook from parameters
     *
     * @param cells         The List of ICells that the created notebook shall contain
     * @param nbformat      The nbformat of the notebook to create
     * @param nbformatMinor The nbformat_minor of the notebook to create
     * @param metadata      The metadata of the notebook that will be created
     * @return An INotebook based on the passed parameters
     */
    public static INotebook createPythonNotebook(List<ICell> cells, Integer nbformat,
                                                 Integer nbformatMinor, IMetadata metadata) {
        INotebook nb = new PythonNotebook();
        nb.setNbformat(nbformat);
        nb.setNbformat_minor(nbformatMinor);
        nb.setMetadata(metadata);
        nb.addCells(cells);
        return nb;
    }

    /**
     * Method for creating a Python notebook using default parameters
     *
     * @param cells The List of ICells that the created notebook shall contain
     * @return A PythonNotebook based on the passed parameters
     */
    public static INotebook createDefaultPythonNotebook(List<ICell> cells) {
        return createPythonNotebook(cells, 4, 5, new PythonMetadata());
    }

    /**
     * Method for creating a Python Notebook cell from the passed information
     *
     * @param source   The List<String> that contains the content of the cell
     * @param cellType The type of the cell
     * @return The created cell
     */
    public static ICell createPythonNotebookCell(List<String> source, CellCategory cellType) {
        ICell cell = new PythonCell();
        cell.setCell_type(cellType);
        cell.addAllToSource(source);
        return cell;
    }
}
