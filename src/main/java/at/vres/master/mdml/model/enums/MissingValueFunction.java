package at.vres.master.mdml.model.enums;

public enum MissingValueFunction {
    DROP_NA("DropNa"),
    FILL_NA("FillNa"),
    INTERPOLATE("Interpolate");

    private final String missingValueFunc;

    MissingValueFunction(String missingValueFunc) {
        this.missingValueFunc = missingValueFunc;
    }

    public String getMissingValueFunc() {
        return missingValueFunc;
    }
}
