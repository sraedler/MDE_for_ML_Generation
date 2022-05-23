package at.vres.master.mdml.output.notebook.impl;

import at.vres.master.mdml.output.notebook.CellCategory;
import at.vres.master.mdml.output.notebook.ICell;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class PythonCell implements ICell {

    private String id = UUID.randomUUID().toString();
    private CellCategory cell_type;
    private Map<String, Object> metadata = new HashMap<>();
    private List<String> source = new LinkedList<>();
    @JsonIgnore
    private final List<String> variables = new LinkedList<>();
    @JsonIgnore
    private String connectedElementName;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCell_type() {
        return cell_type.getValue();
    }

    @Override
    public void setCell_type(CellCategory cell_type) {
        this.cell_type = cell_type;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public List<String> getVariables() {
        return variables;
    }

    @Override
    public String getConnectedElementName() {
        return connectedElementName;
    }

    @Override
    public void addToSource(String toAdd) {
        source.add(toAdd);
    }

    public void setConnectedElementName(String connectedElementName) {
        this.connectedElementName = connectedElementName;
    }

    @Override
    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }
}
