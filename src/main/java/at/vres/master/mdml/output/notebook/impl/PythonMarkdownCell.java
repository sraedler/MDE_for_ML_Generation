package at.vres.master.mdml.output.notebook.impl;

import at.vres.master.mdml.output.notebook.CellCategory;
import at.vres.master.mdml.output.notebook.ICell;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;


public class PythonMarkdownCell implements ICell{

    private String id = UUID.randomUUID().toString();
    private CellCategory cell_type;
    private Map<String, Object> metadata = new HashMap<>();
    private List<String> source = new LinkedList<>();

    @JsonIgnore
    private final List<String> variables = new LinkedList<>();
    @JsonIgnore
    private List<String> connectedElementNames;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCell_type() {
        return this.cell_type.getValue();
    }

    @Override
    public void setCell_type(CellCategory cell_type) {
        this.cell_type = cell_type;
    }

    @Override
    public void addConnectedElementName(String name) {
        this.connectedElementNames.add(name);
    }

    @Override
    public void addConnectedElementNames(List<String> names) {
        this.connectedElementNames.addAll(names);
    }

    @Override
    public Map<String, Object> getMetadata() {
        return this.metadata;
    }

    /**
     * Method for setting the metadata of the cell
     *
     * @param metadata The metadata to set for the cell
     */
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public List<String> getVariables() {
        return this.variables;
    }

    @Override
    public void addVariable(String variable) {
        this.variables.add(variable);
    }

    @Override
    public void addVariables(List<String> variables) {
        this.variables.addAll(variables);
    }

    @Override
    public void addToSource(String toAdd) {
        this.source.add(toAdd);
    }

    @Override
    public void addAllToSource(List<String> allToAdd) {
        this.source.addAll(allToAdd);
    }


    @Override
    public List<String> getSource() {
        return this.source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    @Override
    public List<String> getConnectedElementNames() {
        return this.connectedElementNames;
    }

    /**
     * Method for setting the connectedElementNames of the cell
     *
     * @param connectedElementNames The list of connected element names to set for the cell
     */
    public void setConnectedElementNames(List<String> connectedElementNames) {
        this.connectedElementNames = connectedElementNames;
    }
}
