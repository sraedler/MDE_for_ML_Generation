package at.vres.master.mdml.model;

public class StereotypeNamePair {
    private String stereoName;
    private String attributeName;

    public StereotypeNamePair(String stereoName, String attributeName) {
        this.stereoName = stereoName;
        this.attributeName = attributeName;
    }

    public String getStereoName() {
        return stereoName;
    }

    public void setStereoName(String stereoName) {
        this.stereoName = stereoName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public String toString() {
        return "StereotypeNamePair{" +
                "stereoName='" + stereoName + '\'' +
                ", attributeName='" + attributeName + '\'' +
                '}';
    }
}
