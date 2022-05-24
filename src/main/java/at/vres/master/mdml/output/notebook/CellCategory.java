package at.vres.master.mdml.output.notebook;

/**
 * Enum for the different types a notebook cell can have
 */
public enum CellCategory {
    MARKDOWN("markdown"),
    CODE("code");

    private final String value;

    CellCategory(final String value) {
        this.value = value;
    }

    /**
     * Get the String representation of the value (conforms to how the category is named in notebooks)
     *
     * @return The String representation of the cell type
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
