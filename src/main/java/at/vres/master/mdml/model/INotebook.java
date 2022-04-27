package at.vres.master.mdml.model;

import java.util.List;

public interface INotebook {
    String getName();

    List<ICell> getCells();

    ICell getCellById(String cellId);

    Integer getExecutionCount();

    ICell getCellByElementName(String elementName);
}
