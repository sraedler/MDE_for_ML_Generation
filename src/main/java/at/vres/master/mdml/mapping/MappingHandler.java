package at.vres.master.mdml.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Class for handling the Deserialization of the configuration JSON file
 */
public class MappingHandler {

    /**
     * Returns an UML EObject with the given qualified name from the given UML Model
     *
     * @param qualifiedName The qualified name of the element to return
     * @param modelRoot     The model in which to search for the element
     * @return The element with the given qualified name or null if none could be found in the model
     */
    public static EObject getObjectFromModel(String qualifiedName, Model modelRoot) {
        Iterable<EObject> iterable = modelRoot::eAllContents;
        Optional<EObject> first = StreamSupport.stream(iterable.spliterator(), false).filter(e -> e instanceof NamedElement).filter(ne -> ((NamedElement) ne).getQualifiedName().equals(qualifiedName)).findFirst();
        return first.orElse(null);
    }

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
