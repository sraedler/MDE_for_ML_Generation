package at.vres.master.mdml.model;

public abstract class MLAttributeInput implements ML {
    private String mappedName;

    public String getMappedName() {
        return mappedName;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    abstract public Object getValue();
}
