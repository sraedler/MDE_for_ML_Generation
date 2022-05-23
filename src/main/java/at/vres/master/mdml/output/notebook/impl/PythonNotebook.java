package at.vres.master.mdml.output.notebook.impl;

import at.vres.master.mdml.output.notebook.ICell;
import at.vres.master.mdml.output.notebook.IMetadata;
import at.vres.master.mdml.output.notebook.INotebook;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;

public class PythonNotebook implements INotebook {
    @JsonIgnore
    private String name;
    private final List<ICell> cells = new LinkedList<>();
    @JsonIgnore
    private Integer executionCount = 0;
    private Integer nbformat;
    private Integer nbformat_minor;
    private IMetadata metadata;

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
        return cells.stream().filter(c -> c.getId().equals(cellId)).findFirst().orElse(null);
    }

    @Override
    public Integer getExecutionCount() {
        return executionCount;
    }

    @Override
    public ICell getCellByElementName(String elementName) {
        return cells.stream()
                .filter(c -> c.getConnectedElementNames().contains(elementName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addCell(ICell cellToAdd) {
        cells.add(cellToAdd);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void increaseExecutionCount() {
        executionCount += 1;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    @Override
    public Integer getNbformat() {
        return nbformat;
    }

    @Override
    public void setNbformat(Integer nbformat) {
        this.nbformat = nbformat;
    }

    @Override
    public Integer getNbformat_minor() {
        return nbformat_minor;
    }

    @Override
    public void setNbformat_minor(Integer nbformat_minor) {
        this.nbformat_minor = nbformat_minor;
    }

    @Override
    public IMetadata getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(IMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void addCells(List<ICell> cells) {
        this.cells.addAll(cells);
    }
}
