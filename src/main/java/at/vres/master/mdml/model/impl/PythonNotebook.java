package at.vres.master.mdml.model.impl;

import at.vres.master.mdml.model.ICell;
import at.vres.master.mdml.model.INotebook;

import java.util.LinkedList;
import java.util.List;

public class PythonNotebook implements INotebook {
    private String name;
    private final List<ICell> cells = new LinkedList<>();
    private Integer executionCount = 0;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ICell> getCells() {
        return cells;
    }

    @Override
    public ICell getCellById(String cellId) {
        return cells.stream().filter(c -> c.getCellId().equals(cellId)).findFirst().orElse(null);
    }

    @Override
    public Integer getExecutionCount() {
        return executionCount;
    }

    @Override
    public ICell getCellByElementName(String elementName) {
        return cells.stream().filter(c -> c.getConnectedElementName().equals(elementName)).findFirst().orElse(null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void increaseExecutionCount() {
        executionCount += 1;
    }

}
