package at.vres.master.mdml.output.notebook;

public enum CellCategory {
    MARKDOWN("markdown"),
    CODE("code");

    private final String value;

    CellCategory(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
