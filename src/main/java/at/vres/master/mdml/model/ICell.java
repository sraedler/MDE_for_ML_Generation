package at.vres.master.mdml.model;

import java.util.List;

public interface ICell {
    CellCategory getCategory();

    String getSource();

    String getMetadata();

    String getCellId();

    List<String> getVariableNames();

    String getConnectedElementName();
}
