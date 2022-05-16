package at.vres.master.mdml.tbcg;

import at.vres.master.mdml.mapping.MappingWrapper;
import at.vres.master.mdml.mapping.NameMapping;
import at.vres.master.mdml.mapping.StereotypeMapping;
import at.vres.master.mdml.model.BlockContext;
import at.vres.master.mdml.model.StereotypeNamePair;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.uml2.uml.Class;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class TemplateHandler {
    private static final String VELOCITY_TEMPLATE_PATH_KEY = "file.resource.loader.path";
    private static final String ENCODING = "UTF-8";
    private final Map<Class, BlockContext> contexts;
    private final MappingWrapper mappingWrapper;
    private final String templatePath;

    public TemplateHandler(Map<Class, BlockContext> contexts, MappingWrapper mappingWrapper, String templatePath) {
        this.contexts = contexts;
        this.mappingWrapper = mappingWrapper;
        this.templatePath = templatePath;
    }

    public String execute() {
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        final StringBuilder sb = new StringBuilder();
        p.setProperty(VELOCITY_TEMPLATE_PATH_KEY, templatePath);
        ve.init(p);
        contexts.forEach((key, value) -> {
            List<String> templatesAlreadyMerged = new LinkedList<>();
            VelocityContext context = handleBlockContext(value, new LinkedList<>());
            key.getAppliedStereotypes().forEach(stereo -> {
                StereotypeMapping stereotypeMapping = mappingWrapper.getStereotypeMappings().get(stereo.getName());
                if (stereotypeMapping != null) {
                    if (!templatesAlreadyMerged.contains(stereotypeMapping.getTemplate())) {
                        try (StringWriter writer = new StringWriter()) {
                            ve.mergeTemplate(stereotypeMapping.getTemplate(), ENCODING, context, writer);
                            templatesAlreadyMerged.add(stereotypeMapping.getTemplate());
                            sb.append(writer).append("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            NameMapping nameMapping = mappingWrapper.getNameMappings().get(key.getName());
            if (nameMapping != null) {
                if (!templatesAlreadyMerged.contains(nameMapping.getTemplate())) {
                    try (StringWriter writer = new StringWriter()) {
                        ve.mergeTemplate(nameMapping.getTemplate(), ENCODING, context, writer);
                        templatesAlreadyMerged.add(nameMapping.getTemplate());
                        sb.append(writer).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return sb.toString();
    }

    private VelocityContext handleBlockContext(BlockContext blockContext, List<BlockContext> alreadyHandled) {
        VelocityContext velocityContext = new VelocityContext();
        if (!alreadyHandled.contains(blockContext)) {
            alreadyHandled.add(blockContext);
            blockContext.getPropertyMap().forEach((propName, propVal) -> {
                StereotypeNamePair stereotypeNamePairFromQualifiedName = getStereotypeNamePairFromQualifiedName(propName);
                if (stereotypeNamePairFromQualifiedName != null) {
                    StereotypeMapping stereotypeMapping = mappingWrapper.getStereotypeMappings().get(stereotypeNamePairFromQualifiedName.getStereoName());
                    if (stereotypeMapping != null) {
                        stereotypeMapping.getProperties().forEach((originalName, remappedName) -> {
                            if (originalName.contains("[") && originalName.contains("]")) {
                                String listName = originalName.substring(0, originalName.indexOf("["));
                                String listIndex = originalName.substring(originalName.indexOf("[") + 1, originalName.indexOf("]"));
                                if (listName.equals(stereotypeNamePairFromQualifiedName.getAttributeName())) {
                                    if (propVal instanceof List<?>) {
                                        Object o = ((List<?>) propVal).get(Integer.parseInt(listIndex));
                                        velocityContext.put(remappedName, handlePropValQualifiedName(o));
                                    }
                                }
                            } else if (propVal instanceof List<?>) {
                                velocityContext.put(remappedName, handleValueLists((List<?>) propVal));
                            } else {
                                if (originalName.equals(stereotypeNamePairFromQualifiedName.getAttributeName())) {
                                    velocityContext.put(remappedName, handlePropValQualifiedName(propVal));
                                }
                            }
                        });
                        //String remName = stereotypeMapping.getProperties().get(stereotypeNamePairFromQualifiedName.getAttributeName());
                    }
                } else {
                    if (propVal instanceof List<?>) {
                        velocityContext.put(propName, handleValueLists((List<?>) propVal));
                    } else {
                        velocityContext.put(propName, handlePropValQualifiedName(propVal));
                    }
                }

                NameMapping nameMapping = mappingWrapper.getNameMappings().get(blockContext.getConnectedClass().getName());
                if (nameMapping != null) {
                    nameMapping.getProperties().forEach((originalName, remappedName) -> {
                        if (originalName.contains("[") && originalName.contains("]")) {
                            String listName = originalName.substring(0, originalName.indexOf("["));
                            String listIndex = originalName.substring(originalName.indexOf("[") + 1, originalName.indexOf("]"));
                            if (propVal instanceof List<?>) {
                                Object o = ((List<?>) propVal).get(Integer.parseInt(listIndex));
                                velocityContext.put(remappedName, handlePropValQualifiedName(o));
                            }
                        } else if (propVal instanceof List<?>) {
                            velocityContext.put(remappedName, handleValueLists((List<?>) propVal));
                        } else {
                            velocityContext.put(remappedName, handlePropValQualifiedName(propVal));
                        }
                    });
                }

            });
            blockContext.getLinkedPartContexts().forEach((propName, bcList) -> bcList.forEach(bc -> {
                VelocityContext context = handleBlockContext(bc, alreadyHandled);
                mergeContexts(velocityContext, context);
            }));
        }
        return velocityContext;
    }

    private static Object handlePropValQualifiedName(Object propVal) {
        if (propVal instanceof String) {
            if (((String) propVal).contains("::")) {
                return getNameFromQualifiedName((String) propVal);
            } else {
                return propVal;
            }
        } else {
            return propVal;
        }
    }

    private static String handleValueLists(List<?> list) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        list.forEach(el -> {
            String nameFromQualifiedName = getNameFromQualifiedName(el.toString());
            sb.append("\"").append(nameFromQualifiedName).append("\"").append(", ");
        });
        sb.replace(sb.lastIndexOf(","), sb.length(), "").append("]");
        return sb.toString();
    }

    public static String getNameFromQualifiedName(String qualifiedName) {
        if (qualifiedName != null) {
            if (!qualifiedName.isEmpty()) {
                return qualifiedName.substring(qualifiedName.lastIndexOf("::") + 2);
            } else {
                return "";
            }
        } else {
            return "null";
        }
    }

    private static StereotypeNamePair getStereotypeNamePairFromQualifiedName(String qualifiedName) {
        StereotypeNamePair pair = null;
        if (qualifiedName != null) {
            if (!qualifiedName.isEmpty()) {
                String[] split = qualifiedName.split("::");
                if (split.length > 1) {
                    pair = new StereotypeNamePair(split[split.length - 2], split[split.length - 1]);
                }
            }
        }
        return pair;
    }

    private static void mergeContexts(VelocityContext superContext, final VelocityContext contextToMerge) {
        for (String key : contextToMerge.getKeys()) {
            Object o = contextToMerge.get(key);
            if (!superContext.containsKey(key)) {
                superContext.put(key, o);
            }
        }
    }


}
