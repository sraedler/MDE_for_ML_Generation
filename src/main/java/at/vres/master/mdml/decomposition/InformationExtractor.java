package at.vres.master.mdml.decomposition;

import MLModel.Attributes.ML_Attribute_Input;
import MLModel.ML;
import at.vres.master.mdml.model.BlockContext;
import at.vres.master.mdml.utils.EMFResourceLoader;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InformationExtractor {
    private static final String ENTRY_POINT = "entryPoint";
    private static final String CONNECTION_STEREOTYPE_NAME = "ML_Block_Connection";
    private static final String ATTRIBUTE_STEREOTYPE_NAME = "ML_Attribute_Input";
    private static final List<String> stereotypesToIgnore = new LinkedList<>(List.of("Block"));
    private final Map<Class, BlockContext> existingContexts;
    private final String loadedModelPath;
    private Model loadedModel;
    private Integer executionCounter;

    public InformationExtractor(String modelPath) {
        existingContexts = new HashMap<>();
        loadedModelPath = modelPath;
        init();
        executionCounter = 0;
    }

    public InformationExtractor(Map<Class, BlockContext> existingContexts, String modelPath) {
        this.existingContexts = existingContexts;
        loadedModelPath = modelPath;
        init();
        executionCounter = 0;
    }

    public void init() {
        loadedModel = getModel(loadedModelPath, true);
    }

    public Map<Class, BlockContext> getContextsForStateMachine(String stateMachineName) {
        List<Class> blocks = getOrderedBlocksFromModelAndStateMachineDiagram(loadedModel, stateMachineName);
        blocks.forEach(block -> {
            createContextForBlock(block);
            existingContexts.get(block).setExecutionOrder(executionCounter);
            executionCounter += 1;
        });
        return existingContexts.entrySet().stream().sorted(new ContextComparator())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void contextPropertyHandling(Property prop, BlockContext bc) {
        Type type = prop.getType();
        if (type instanceof Class) {
            BlockContext blockContext = existingContexts.get(type);
            if (blockContext == null) {
                createContextForBlock((Class) type);
                bc.addLinkedBlockContext(type.getQualifiedName(), existingContexts.get(type));
            } else {
                if (!bc.getLinkedPartContexts().containsKey(type.getQualifiedName())) {
                    bc.addLinkedBlockContext(type.getQualifiedName(), blockContext);
                }
            }
        } else if (checkForPrimitiveValueClass(type.getName())) {
            // TODO check if the Default String is enough (since we just insert into templates) or if I need to handle the ValueSpecification
            if (!bc.getPropertyMap().containsKey(prop.getQualifiedName())) {
                bc.getPropertyMap().put(prop.getQualifiedName(), prop.getDefault());
                prop.getAppliedStereotypes().stream()
                        .filter(st -> !stereotypesToIgnore.contains(st.getName()))
                        .forEach(stereo -> contextStereotypeHandling(stereo, bc, prop, prop.getName() + "__"));
            }
        }
    }

    private void contextStereotypeHandling(Stereotype stereo, BlockContext bc, Element el, String prefix) {
        stereo.getAllAttributes().forEach(att -> {
            Object value = el.getValue(stereo, att.getName());
            if (value != null) {
                java.lang.Class<?> aClass = value.getClass();
                if (checkForPrimitiveValueClass(aClass.getSimpleName())) {
                    bc.getPropertyMap().put(prefix + att.getQualifiedName(), value);
                    bc.addStereotypeAttributeMapping(stereo.getQualifiedName(), att.getQualifiedName());
                } else if (value instanceof List<?>) {
                    if (!((List<?>) value).isEmpty()) {
                        Object o = ((List<?>) value).get(0);
                        java.lang.Class<?> oClass = o.getClass();
                        if (o instanceof Class) {
                            ((List<?>) value).forEach(clazz -> {
                                createContextForBlock((Class) clazz);
                                bc.addLinkedBlockContext(prefix + att.getQualifiedName(), existingContexts.get(clazz));
                                bc.addStereotypeAttributeMapping(stereo.getQualifiedName(), att.getQualifiedName());
                            });
                        } else if (o instanceof Property) {
                            ((List<?>) value).forEach(prop -> contextPropertyHandling((Property) prop, bc));
                            bc.addStereotypeAttributeMapping(stereo.getQualifiedName(), att.getQualifiedName());
                        } else if (checkForPrimitiveValueClass(oClass.getSimpleName())) {
                            bc.getPropertyMap().put(prefix + att.getQualifiedName(), value);
                            bc.addStereotypeAttributeMapping(stereo.getQualifiedName(), att.getQualifiedName());
                        } else if (o instanceof Element) {
                            ((Element) o).getAppliedStereotypes().stream()
                                    .filter(st -> st.getName().equals(ATTRIBUTE_STEREOTYPE_NAME))
                                    .findAny()
                                    .ifPresent(stereotype ->
                                            contextStereotypeHandling(stereo, bc, (Element) o, prefix + att.getName() + "__")
                                    );
                        } else if (o instanceof ML_Attribute_Input) {
                            final List<String> qualNames = new LinkedList<>();
                            ((List<?>) value).forEach(mli -> {
                                contextPropertyHandling(((ML_Attribute_Input) mli).getBase_Property(), bc);
                                qualNames.add(((ML_Attribute_Input) mli).getBase_Property().getQualifiedName());
                            });
                            bc.getPropertyMap().put(prefix + att.getQualifiedName(), qualNames);
                            bc.addStereotypeAttributeMapping(stereo.getQualifiedName(), att.getQualifiedName());
                        } else {
                            System.out.println("ZZZ" + o + " class: " + o.getClass());
                        }
                    }
                } else if (value instanceof Property) {
                    contextPropertyHandling((Property) value, bc);
                    bc.addStereotypeAttributeMapping(stereo.getQualifiedName(), att.getQualifiedName());
                } else if (value instanceof Class) {
                    createContextForBlock((Class) value);
                    bc.addLinkedBlockContext(prefix + att.getQualifiedName(), existingContexts.get(value));
                } else if (value instanceof Element) {
                    ((Element) value).getAppliedStereotypes().stream()
                            .filter(st -> st.getName().equals(ATTRIBUTE_STEREOTYPE_NAME))
                            .findAny()
                            .ifPresent(stereotype ->
                                    contextStereotypeHandling(stereo, bc, (Element) value, prefix + att.getName() + "__")
                            );
                } else if (value instanceof ML_Attribute_Input) {
                    contextPropertyHandling(((ML_Attribute_Input) value).getBase_Property(), bc);
                    bc.getPropertyMap().put(prefix + att.getQualifiedName(), ((ML_Attribute_Input) value).getBase_Property().getQualifiedName());
                    bc.addStereotypeAttributeMapping(stereo.getQualifiedName(), att.getQualifiedName());
                } else {
                    System.out.println(att.getQualifiedName() + " is currently not handled!");
                }
            } else {
                System.out.println("VALUE IS NULL FOR" + att.getQualifiedName());
            }
        });
    }

    private void createContextForBlock(Class clazz) {
        if (!existingContexts.containsKey(clazz)) {
            BlockContext bc = new BlockContext(clazz);
            existingContexts.put(clazz, bc);
            clazz.getAllAttributes().forEach(att -> {
                System.out.println("att = " + att);
                contextPropertyHandling(att, bc);
            });
            clazz.getAppliedStereotypes()
                    .stream()
                    .filter(st -> !stereotypesToIgnore.contains(st.getName()))
                    .forEach(stereo -> {
                        if (stereo.getName().equals("Train_Test_Split")) {
                            System.out.println("Maybe now?");
                        }
                        contextStereotypeHandling(stereo, bc, clazz, "");
                    });
        }
    }

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

    public static List<Class> getOrderedBlocksFromModelAndStateMachineDiagram(final Model model, final String stateMachineName) {
        final List<Class> orderedBlocks = new LinkedList<>();
        StateMachine stateMachine = (StateMachine) getStreamFromIterator(model.eAllContents(), false)
                .filter(eo -> eo instanceof StateMachine)
                .filter(st -> ((StateMachine) st).getName().equals(stateMachineName))
                .findAny().orElse(null);
        System.out.println("stateMachine = " + stateMachine);
        if (stateMachine != null) {
            stateMachine.getConnectionPoints().stream()
                    .filter(e -> e.getKind().getLiteral().equals(ENTRY_POINT)).
                    findFirst().ifPresent(
                            ps -> ps.getOutgoings().forEach(
                                    outgoing -> followVertexForBlock(outgoing.getTarget(), 0, orderedBlocks)
                            )
                    );
        }
        return orderedBlocks;
    }

    public static void handleStereotype(final Stereotype stereo, final Class clazz) {
        stereo.getAllAttributes().forEach(att -> {
            Object value = clazz.getValue(stereo, att.getName());
            if (value != null) {

            }

        });
    }

    public static void handleClazz(final Class clazz) {
        // handle stereotypes
        clazz.getAppliedStereotypes().stream().filter(st -> !stereotypesToIgnore.contains(st.getName())).forEach(s -> handleStereotype(s, clazz));
        // handle attributes
        clazz.getAllAttributes().forEach(InformationExtractor::handleProperty);
    }

    public static void handleProperty(final Property prop) {

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

    public static void followVertexForBlock(final Vertex state, final int depth, List<Class> orderedBlocks) {
        Stereotype stereotype = state.getAppliedStereotypes().stream()
                .filter(s -> s.getName().equals(CONNECTION_STEREOTYPE_NAME)).findAny().orElse(null);
        if (stereotype != null) {
            Object ml_block = state.getValue(stereotype, "ML_Block");
            if (ml_block instanceof ML) {
                Class clazz = ((ML) ml_block).getBase_Class();
                orderedBlocks.add(clazz);
            }
        }
        state.getOutgoings().forEach(out -> followVertexForBlock(out.getTarget(), depth + 1, orderedBlocks));
    }

    public static Map<String, String> startInformationExtraction(final Class clazz) {
        final Map<String, String> paramToInfoMap = new HashMap<>();
        clazz.getOwnedAttributes().forEach(att -> {
            if (att != null) {
                paramToInfoMap.put(att.getName(), att.getDefault());
            }
        });
        clazz.getAppliedStereotypes().stream()
                .filter(s -> !stereotypesToIgnore.contains(s.getName()))
                .forEach(stereo -> stereo.getAllAttributes().forEach(satt -> {
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
                }));
        return paramToInfoMap;
    }

    public static Boolean checkForPrimitiveValueClass(String simpleClassName) {
        final List<String> primitiveClassNames = new LinkedList<>(List.of("String", "Integer", "Boolean", "Double", "UnlimitedNatural", "Real"));
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
