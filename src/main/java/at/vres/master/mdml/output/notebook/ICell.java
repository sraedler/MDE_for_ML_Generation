package at.vres.master.mdml.output.notebook;

import java.util.List;

public interface ICell {
    CellCategory getCategory();

    String getSource();

    String getMetadata();

    String getCellId();

    List<String> getVariableNames();

    String getConnectedElementName();
}
