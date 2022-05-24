package at.vres.master.mdml.output.notebook.impl;

import at.vres.master.mdml.output.notebook.IMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * A specific implementation of the IMetadata interface for Python notebooks
 */
public class PythonMetadata implements IMetadata {
    private Map<String, String> kernelspec = new HashMap<>(Map.of("display_name", "Python 3",
            "language", "python", "name", "python3"));
    private Map<String, Object> language_info = new HashMap<>(Map.of(
            "codemirror_mode", new HashMap<>(Map.of(
                    "name", "ipython", "version", 3)
            ),
            "file_extension", ".py", "mimetype", "text/x-python", "name", "python",
            "nbconvert_exporter", "python", "pygments_lexer", "ipython3", "version", "3.7.12")
    );

    @Override
    public Map<String, String> getKernelspec() {
        return kernelspec;
    }

    @Override
    public Map<String, Object> getLanguage_info() {
        return language_info;
    }

    /**
     * Method for setting the language info of the metadata
     *
     * @param language_info The language info to set for the metadata
     */
    public void setLanguage_info(Map<String, Object> language_info) {
        this.language_info = language_info;
    }

    /**
     * Method for setting the kernelspec of the metadata
     *
     * @param kernelspec The kernelspec to set for the metadata
     */
    public void setKernelspec(Map<String, String> kernelspec) {
        this.kernelspec = kernelspec;


    }
}
