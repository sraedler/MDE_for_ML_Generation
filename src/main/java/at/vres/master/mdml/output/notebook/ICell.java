package at.vres.master.mdml.output.notebook;

import java.util.List;
import java.util.Map;

public interface ICell {
    String getCell_type();

    List<String> getSource();

    Map<String, Object> getMetadata();

    String getId();

    String getConnectedElementName();

    void addToSource(String toAdd);

    void setCell_type(CellCategory cell_type);
}
