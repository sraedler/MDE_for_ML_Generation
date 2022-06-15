package at.vres.master.mdml.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Class for handling the Deserialization of the configuration JSON file
 */
public class MappingHandler {

    /**
     * Deserialize a JSON file to a MappingWrapper object
     *
     * @param jsonPath The path to the JSON file to deserialize
     * @return The deserialized JSON as a MappingWrapper object
     */
    public static MappingWrapper readJSONV2(String jsonPath) {
        ObjectMapper mapper = new ObjectMapper();
        MappingWrapper result = null;
        try {
            result = mapper.readValue(new File(jsonPath), MappingWrapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
