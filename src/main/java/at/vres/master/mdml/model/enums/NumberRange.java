package at.vres.master.mdml.model.enums;

public enum NumberRange {
    BINARY("Binary"),
    MULTI_VARIANT("Multivariant"),
    INTERVAL("Interval"),
    ANY("Any");

    private final String numberRange;

    NumberRange(String numberRange) {
        this.numberRange = numberRange;
    }

    public String getNumberRange() {
        return numberRange;
    }
}
