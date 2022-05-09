package at.vres.master.mdml.decomposition;

import MLModel.ML;
import at.vres.master.mdml.utils.ContextResolver;
import at.vres.master.mdml.utils.EMFResourceLoader;
import org.eclipse.emf.common.notify.Notifier;
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
                List<String> alreadyVisited = new LinkedList<>();
                evaluateBlock(clazz, alreadyVisited);
                //Map<String, String> stringStringMap = startInformationExtraction(clazz);
                //contextMap.put(clazz, stringStringMap);
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
                    if (checkForPrimitiveValueClass(value.getClass().getSimpleName())) {
                        paramToInfoMap.put(satt.getName(), value.toString());
                    } else if (value instanceof Class) {
                        Map<String, String> stringStringMap = startInformationExtraction((Class) value);
                        paramToInfoMap.putAll(stringStringMap);
                    } else if (value instanceof List<?>) {
                        if (!((List<?>) value).isEmpty()) {
                            Object o = ((List<?>) value).get(0);
                            if (o != null) {
                                if (checkForPrimitiveValueClass(o.getClass().getSimpleName())) {
                                    final StringBuilder sb = new StringBuilder("[");
                                    ((List<?>) value).forEach(v -> sb.append("\"").append(v).append("\"").append(","));
                                    paramToInfoMap.put(satt.getName(), sb.toString());
                                }
                            }
                        }
                    } else {
                        System.out.println("Could not handle Object: " + value);
                    }
                }
            });
        });
        return paramToInfoMap;
    }

    public static Boolean checkForPrimitiveValueClass(String simpleClassName) {
        final List<String> primitiveClassNames = new LinkedList<>(List.of("String", "Integer", "Boolean", "Double", "UnlimitedNatural"));
        return primitiveClassNames.contains(simpleClassName);
    }

    public static void evaluateBlock(Class clazz, List<String> alreadyVisited) {
        alreadyVisited.add(clazz.getQualifiedName());
        clazz.getAppliedStereotypes().forEach(s -> evaluateStereotype(s, clazz, alreadyVisited));
        clazz.getOwnedAttributes().forEach(att -> {
            att.getAppliedStereotypes().forEach(st -> evaluateStereotype(st, att, alreadyVisited));
            Type type = att.getType();
            if (type instanceof PrimitiveType) {
                System.out.println("PrimitiveType = " + att.getName());
            } else if (type instanceof Class) {
                System.out.println("Class = " + att.getClass_());
                if (!alreadyVisited.contains(att.getClass_().getQualifiedName()))
                    evaluateBlock(att.getClass_(), alreadyVisited);
            } else if (type instanceof Property) {
                evaluateProperty(att);
            }
            //System.out.println("type = " + type);
        });
    }

    public static void evaluateProperty(Property property) {
        String aDefault = property.getDefault();
        System.out.println("aDefault = " + aDefault);
    }

    public static void evaluateStereotype(Stereotype stereo, Element el, List<String> alreadyVisited) {
        if (!stereotypesToIgnore.contains(stereo.getName())) {
            stereo.getAllAttributes().forEach(att -> {
                Object value = el.getValue(stereo, att.getName());
                if (value instanceof Property) {
                    evaluateProperty((Property) value);
                } else if (value instanceof Class) {
                    // TODO fix stack overflow due to circular function calls
                    if (!alreadyVisited.contains(((Class) value).getQualifiedName()))
                    evaluateBlock((Class) value, alreadyVisited);
                } else {
                    System.out.println("Stereotype Attribute: " + att.getName() + " = " + value);
                }

            });
        }
    }


}
