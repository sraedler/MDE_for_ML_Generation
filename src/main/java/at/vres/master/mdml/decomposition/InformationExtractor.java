package at.vres.master.mdml.decomposition;

import MLModel.ML;
import at.vres.master.mdml.utils.EMFResourceLoader;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InformationExtractor {
    private static final String ENTRY_POINT = "entryPoint";
    private static final String CONNECTION_STEREOTYPE_NAME = "ML_Block_Connection";
    private static final List<String> stereotypesToIgnore = new LinkedList<>(List.of("Block"));


    public static Model getModel(final String modelPath, final Boolean initBaseResources) {
        if (initBaseResources) EMFResourceLoader.initBaseResources();
        ResourceSet resourceSet = EMFResourceLoader.loadModel(modelPath);
        Iterable<Notifier> iterable = resourceSet::getAllContents;
        return (Model) StreamSupport.stream(iterable.spliterator(), false)
                .filter(con -> con instanceof Model)
                .findAny().orElse(null);
    }

    public static <T> Stream<T> getStreamFromIterator(final Iterator<T> it, final Boolean parallel) {
        Iterable<T> iterable = () -> it;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

    public static Map<Class, Map<String, String>> doExtraction(final Model model, final String stateMachineName) {
        final Map<Class, Map<String, String>> contextMap = new LinkedHashMap<>();
        StateMachine stateMachine = (StateMachine) getStreamFromIterator(model.eAllContents(), false)
                .filter(eo -> eo instanceof StateMachine)
                .filter(st -> ((StateMachine) st).getName().equals(stateMachineName))
                .findAny().orElse(null);
        if (stateMachine != null) {
            stateMachine.getConnectionPoints().stream()
                    .filter(e -> e.getKind().getLiteral().equals(ENTRY_POINT)).
                    findFirst().ifPresent(
                            ps -> ps.getOutgoings().forEach(
                                    outgoing -> followVertex(outgoing.getTarget(), 0, contextMap)
                            )
                    );
        }
        return contextMap;
    }

    public static void followVertex(final Vertex state, final int depth, Map<Class, Map<String, String>> contextMap) {
        Stereotype stereotype = state.getAppliedStereotypes().stream()
                .filter(s -> s.getName().equals(CONNECTION_STEREOTYPE_NAME)).findAny().orElse(null);
        if (stereotype != null) {
            Object ml_block = state.getValue(stereotype, "ML_Block");
            if (ml_block instanceof ML) {
                Class clazz = ((ML) ml_block).getBase_Class();
                Map<String, String> stringStringMap = startInformationExtraction(clazz);
                contextMap.put(clazz, stringStringMap);
            }
        }
        state.getOutgoings().forEach(out -> followVertex(out.getTarget(), depth + 1, contextMap));
    }

    public static Map<String, String> startInformationExtraction(final Class clazz) {
        final Map<String, String> paramToInfoMap = new HashMap<>();
        clazz.getOwnedAttributes().forEach(att -> {
            if (att != null) {
                paramToInfoMap.put(att.getName(), att.getDefault());
            }
        });
        clazz.getAppliedStereotypes().stream().filter(s -> !stereotypesToIgnore.contains(s.getName())).forEach(stereo -> {
            stereo.getAllAttributes().forEach(satt -> {
                Object value = clazz.getValue(stereo, satt.getName());
                if (value != null) {
                    if (value instanceof Class) {
                        System.out.println("CLASS!");
                    } else if (value instanceof List<?>) {
                        if (!((List<?>) value).isEmpty()) {
                            Object o = ((List<?>) value).get(0);
                            if (o != null) {
                                if (o instanceof Class) {
                                    // TODO handle class in list
                                    System.out.println("CLASS IN LIST");
                                }
                            }
                        }
                    } else {
                        java.lang.Class<?> aClass = value.getClass();
                        System.out.println("aClass = " + aClass.getName());
                        paramToInfoMap.put(satt.getName(), value.toString());
                    }
                }
            });
        });
        return paramToInfoMap;
    }


}
