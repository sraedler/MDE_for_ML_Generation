package at.vres.master.mdml.model;

/**
 * Class for holding the name of a Stereotype and the name of an attribute of this Stereotype
 */
public class StereotypeNamePair {
    private String stereoName;
    private String attributeName;

    public StereotypeNamePair(String stereoName, String attributeName) {
        this.stereoName = stereoName;
        this.attributeName = attributeName;
    }

    /**
     * Get the name of the Stereotype
     *
     * @return The name of the Stereotype as a String
     */
    public String getStereoName() {
        return stereoName;
    }

    /**
     * Set the name of the Stereotype
     *
     * @param stereoName The name of the Stereotype to set
     */
    public void setStereoName(String stereoName) {
        this.stereoName = stereoName;
    }

    /**
     * Get the name of the attribute of the Stereotype
     *
     * @return The name of the attribute of the Stereotype
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Set the name of the attribute of the stereotype
     *
     * @param attributeName The name of the attribute to set
     */
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
