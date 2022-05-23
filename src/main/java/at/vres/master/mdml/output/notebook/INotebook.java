package at.vres.master.mdml.output.notebook;

import java.util.List;

public interface INotebook {
    String getName();

    void setName(String name);

    List<ICell> getCells();

    ICell getCellById(String cellId);

    Integer getExecutionCount();

    ICell getCellByElementName(String elementName);

    void addCell(ICell cellToAdd);

    Integer getNbformat();

    Integer getNbformat_minor();

    void setNbformat(Integer nbformat);

    void setNbformat_minor(Integer nbformat_minor);

    IMetadata getMetadata();

    void setMetadata(IMetadata metadata);

    void addCells(List<ICell> cells);
}
