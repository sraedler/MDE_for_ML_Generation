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

public class NotebookGenerator {

    public static ICell mergeCellIntoCell(ICell first, ICell second) {
        if (first.getCell_type().equals(second.getCell_type())) {  // only merge cells of the same type
            first.addConnectedElementNames(second.getConnectedElementNames());
            first.addToSource("\n");
            first.addAllToSource(second.getSource());
            first.addVariables(second.getVariables());
        }
        return first;
    }

    public static void generateTo(String path, INotebook notebook) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(path), notebook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static INotebook createPythonNotebook(List<ICell> cells, Integer nbformat,
                                                 Integer nbformatMinor, IMetadata metadata) {
        INotebook nb = new PythonNotebook();
        nb.setNbformat(nbformat);
        nb.setNbformat_minor(nbformatMinor);
        nb.setMetadata(metadata);
        nb.addCells(cells);
        return nb;
    }

    public static INotebook createDefaultPythonNotebook(List<ICell> cells) {
        return createPythonNotebook(cells, 4, 5, new PythonMetadata());
    }

    public static ICell createPythonNotebookCell(List<String> source, CellCategory cellType) {
        ICell cell = new PythonCell();
        cell.setCell_type(cellType);
        cell.addAllToSource(source);
        return cell;
    }
}
