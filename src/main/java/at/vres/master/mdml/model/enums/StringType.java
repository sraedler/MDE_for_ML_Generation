package at.vres.master.mdml.model.enums;

public enum StringType {
    ANY("Any"),
    REGEX_PATTERN("RegexPattern"),
    SERIALIZED_JSON("SerializedJson");

    private final String stringType;

    StringType(String stringType) {
        this.stringType = stringType;
    }

    public String getStringType() {
        return stringType;
    }
}
