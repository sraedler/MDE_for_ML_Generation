package at.vres.master.mdml.mapping;

import at.vres.master.mdml.decomposition.MLInformationHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public static SimpleJSONInfoHolder readJSONSimple(String jsonPath) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleJSONInfoHolder result = null;
        try {
            result = mapper.readValue(new File(jsonPath), SimpleJSONInfoHolder.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, String> resolveMappingFromModel(SimpleJSONInfoHolder json, Map<String, MLInformationHolder> modelMap) {
        Map<String, String> transformMap = new HashMap<>();
        json.getMappings().forEach(m -> m.getParameters().forEach((key, value) -> {
            String[] split = value.split("&&");
            System.out.println("split = " + Arrays.toString(split));
            switch (split.length) {
                case 1 -> transformMap.put(key, value);
                case 2 -> {
                    MLInformationHolder mlInformationHolder = modelMap.values().stream().filter(ih -> ih.getName().equals(split[0])).findFirst().orElse(null);
                    Object o = mlInformationHolder != null ? mlInformationHolder.getProperties().get(split[1]) : null;
                    transformMap.put(key, (String) o);
                }
                case 3 -> {
                    MLInformationHolder modIh = modelMap.values().stream().filter(ih -> ih.getName().equals(split[0])).findFirst().orElse(null);
                    if (modIh != null) {
                        Map<String, Object> stringObjectMap = modIh.getStereotypes().get(split[1]);
                        Object o1 = stringObjectMap.get(split[2]);
                        transformMap.put(key, (String) o1);
                    }
                }
                default -> System.out.println("Not a properly formed JSON String!");
            }
        }));
        return transformMap;
    }


}
