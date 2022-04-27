package at.vres.master.mdml.model.enums;

public enum MLDatatype {
    INTEGER("Integer"),
    FLOAT("Float"),
    STRING("String"),
    DATETIME("Datetime"),
    BOOLEAN("Boolean");

    private final String datatype;

    MLDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getDatatype() {
        return datatype;
    }
}
