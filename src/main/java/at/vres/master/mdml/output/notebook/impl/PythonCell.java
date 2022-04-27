package at.vres.master.mdml.output.notebook.impl;

import at.vres.master.mdml.output.notebook.CellCategory;
import at.vres.master.mdml.output.notebook.ICell;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PythonCell implements ICell {

    private static final String cellId = UUID.randomUUID().toString();
    private static final CellCategory category = CellCategory.CODE;
    private String source;
    private String metadata;
    private final List<String> variables = new LinkedList<>();
    private String connectedElementName;

    @Override
    public CellCategory getCategory() {
        return category;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getMetadata() {
        return metadata;
    }

    @Override
    public String getCellId() {
        return cellId;
    }

    @Override
    public List<String> getVariableNames() {
        return variables;
    }

    @Override
    public String getConnectedElementName() {
        return connectedElementName;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void addVariableName(String varName) {
        variables.add(varName);
    }

    public Boolean containsVarName(String varName) {
        return variables.contains(varName);
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public void setConnectedElementName(String connectedElementName) {
        this.connectedElementName = connectedElementName;
    }
}
