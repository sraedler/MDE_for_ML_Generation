package at.vres.master.mdml.output.notebook;

import java.util.Map;

/**
 * Interface for metadata objects for notebooks
 */
public interface IMetadata {

    /**
     * Get the kernelspec map (contains information regarding the notebook kernel)
     *
     * @return A Map<String, String> containing the kernelspec information of the notebook
     */
    Map<String, String> getKernelspec();

    /**
     * Get the map containing the language information of the notebook
     *
     * @return A Map<String, Object> containing the lanugage information of the notebook
     */
    Map<String, Object> getLanguage_info();

}
