package at.vres.master.mdml.output.notebook;

import java.util.List;
import java.util.Map;

public interface ICell {
    String getCell_type();

    List<String> getSource();

    Map<String, Object> getMetadata();

    String getId();

    List<String> getConnectedElementNames();

    void addToSource(String toAdd);

    void addAllToSource(List<String> allToAdd);

    void setCell_type(CellCategory cell_type);

    void addConnectedElementName(String name);

    void addConnectedElementNames(List<String> names);

    List<String> getVariables();

    void addVariable(String variable);

    void addVariables(List<String> variables);
}
