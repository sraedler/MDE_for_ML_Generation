package at.vres.master.mdml.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class MappingHandler {

    public static EObject getObjectFromModel(String qualifiedName, Model modelRoot) {
       Iterable<EObject> iterable = modelRoot::eAllContents;
        Optional<EObject> first = StreamSupport.stream(iterable.spliterator(), false).filter(e -> e instanceof NamedElement).filter(ne -> ((NamedElement) ne).getQualifiedName().equals(qualifiedName)).findFirst();
        return first.orElse(null);
    }

    public static JSONInformationHolder readJSON(String jsonPath) {
        ObjectMapper mapper = new ObjectMapper();
        JSONInformationHolder result = null;
        try {
            result = mapper.readValue(new File(jsonPath), JSONInformationHolder.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
