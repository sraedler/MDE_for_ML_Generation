package at.vres.master.mdml.output.notebook;

import java.util.HashMap;
import java.util.Map;

public interface IMetadata {

    Map<String, String> getKernelspec();

    Map<String, Object> getLanguage_info();

}
