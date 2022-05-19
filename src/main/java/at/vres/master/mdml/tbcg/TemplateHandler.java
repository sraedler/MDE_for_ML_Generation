package at.vres.master.mdml.tbcg;

import at.vres.master.mdml.mapping.MappingWrapper;
import at.vres.master.mdml.mapping.NameMapping;
import at.vres.master.mdml.mapping.StereotypeMapping;
import at.vres.master.mdml.model.BlockContext;
import at.vres.master.mdml.model.StereotypeNamePair;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.uml2.uml.Class;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateHandler {
    private static final String VELOCITY_TEMPLATE_PATH_KEY = "file.resource.loader.path";
    private static final String ENCODING = "UTF-8";
    private final Map<Class, BlockContext> contexts;
    private final MappingWrapper mappingWrapper;
    private final String templatePath;
    private static final String KEYWORD_OWNER = "OWNER";
    private static final String KEYWORD_SEPARATOR = "\\.";
    private static final String KEYWORD_THIS = "THIS";
    private static final String KEYWORD_BLOCK = "BLOCK";
    private static final String KEYWORD_NAME = "NAME";
    private static final String KEYWORD_CONNECTION = "CONNECTED";
    private static final String DEFAULT_VALUE_REGEX = "\\$\\{\\(([a-zA-Z0-9_.-]*),\"?([a-zA-Z0-9_.-]*)\"?\\)}";
    private static final String KEYWORD_PROPNAME = "PROPNAME";
    private static final String PROPNAME_QUALIFIED_NAME_SEPARATOR = "__";
    private static final String QUALIFIED_NAME_SEPARATOR = "::";
    private static final String START_TEMPLATE_VARIABLE = "${";
    private static final String END_TEMPLATE_VARIABLE = "}";

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
                    if (!mappingWrapper.getBlockedStereotypes().contains(stereo.getName()) && !mappingWrapper.getBlockedNames().contains(key.getName())) {
                        if (!templatesAlreadyMerged.contains(stereotypeMapping.getTemplate())) {
                            String templateString = handleTemplate(context, templatePath + "//" + stereotypeMapping.getTemplate());
                            try (StringWriter writer = new StringWriter()) {
                                //ve.mergeTemplate(stereotypeMapping.getTemplate(), ENCODING, context, writer);
                                ve.evaluate(context, writer, stereotypeMapping.getTemplate(), templateString);
                                templatesAlreadyMerged.add(stereotypeMapping.getTemplate());
                                sb.append(writer).append("\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            NameMapping nameMapping = mappingWrapper.getNameMappings().get(key.getName());
            if (nameMapping != null) {
                if (!mappingWrapper.getBlockedNames().contains(key.getName())) {
                    if (!templatesAlreadyMerged.contains(nameMapping.getTemplate())) {
                        String executeWith = nameMapping.getExecuteWith();
                        if (executeWith == null || executeWith.equals(key.getName())) {
                            try (StringWriter writer = new StringWriter()) {
                                ve.mergeTemplate(nameMapping.getTemplate(), ENCODING, context, writer);
                                templatesAlreadyMerged.add(nameMapping.getTemplate());
                                sb.append(writer).append("\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        return sb.toString();
    }

    private String handleTemplate(VelocityContext context, String templateFilePath) {
        final StringBuilder templateString = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(templateFilePath)))) {
            String line;
            Pattern pattern = Pattern.compile(DEFAULT_VALUE_REGEX);
            int findCount = 0;
            while ((line = br.readLine()) != null) {
                String toMatch = line;
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    if (findCount == 0) {
                        findCount += 1;
                        String paramName = matcher.group(1);
                        String defaultVal = matcher.group(2);
                        Object o = context.get(paramName);
                        if (o == null) {
                            context.put(paramName, defaultVal.replace("\"", ""));
                        }
                        templateString.append(line, matcher.regionStart(), matcher.start()).append(START_TEMPLATE_VARIABLE)
                                .append(paramName)
                                .append(END_TEMPLATE_VARIABLE);
                        toMatch = line.substring(matcher.end(), matcher.regionEnd());
                    } else {
                        Matcher matcherTwo = pattern.matcher(toMatch);
                        if (matcherTwo.find()) {
                            String paramName = matcherTwo.group(1);
                            String defaultVal = matcherTwo.group(2);
                            Object o = context.get(paramName);
                            if (o == null) {
                                context.put(paramName, defaultVal.replace("\"", ""));
                            }

                            templateString.append(toMatch, matcherTwo.regionStart(), matcherTwo.start()).append(START_TEMPLATE_VARIABLE)
                                    .append(paramName)
                                    .append(END_TEMPLATE_VARIABLE);
                            toMatch = toMatch.substring(matcherTwo.end(), matcherTwo.regionEnd());
                        }
                    }
                }
                if (findCount == 0) {
                    templateString.append(line);
                } else {
                    templateString.append(toMatch);
                }
                templateString.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateString.toString();
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
                                        if (originalName.contains(KEYWORD_OWNER)) {
                                            Object o1 = handleOwner(blockContext, originalName, o);
                                            velocityContext.put(remappedName, handlePropValQualifiedName(o1));
                                        } else {
                                            velocityContext.put(remappedName, handlePropValQualifiedName(o));
                                        }
                                    }
                                }
                            } else if (propVal instanceof List<?>) {
                                if (originalName.equals(stereotypeNamePairFromQualifiedName.getAttributeName())) {
                                    if (originalName.contains(KEYWORD_OWNER)) {
                                        Object o1 = handleOwner(blockContext, originalName, propVal);
                                        velocityContext.put(remappedName, handlePropValQualifiedName(o1));
                                    } else {
                                        velocityContext.put(remappedName, handleValueLists((List<?>) propVal));
                                    }
                                }
                            } else {
                                if (originalName.equals(stereotypeNamePairFromQualifiedName.getAttributeName())) {
                                    if (originalName.contains(KEYWORD_OWNER)) {
                                        Object o1 = handleOwner(blockContext, originalName, propVal);
                                        velocityContext.put(remappedName, handlePropValQualifiedName(o1));
                                    } else {
                                        velocityContext.put(remappedName, handlePropValQualifiedName(propVal));
                                    }
                                }
                            }
                        });
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
                    String shortPropName = getNameFromQualifiedName(propName);
                    nameMapping.getProperties().forEach((originalName, remappedName) -> {
                        if (originalName.contains("[") && originalName.contains("]")) {
                            String listName = originalName.substring(0, originalName.indexOf("["));
                            String listIndex = originalName.substring(originalName.indexOf("[") + 1, originalName.indexOf("]"));
                            if (listName.equals(shortPropName)) {
                                if (propVal instanceof List<?>) {
                                    Object o = ((List<?>) propVal).get(Integer.parseInt(listIndex));
                                    if (originalName.contains(KEYWORD_OWNER)) {
                                        Object o1 = handleOwner(blockContext, originalName, propVal);
                                        velocityContext.put(remappedName, handlePropValQualifiedName(o1));
                                    } else {
                                        velocityContext.put(remappedName, handlePropValQualifiedName(o));
                                    }
                                }
                            }
                        } else if (shortPropName.equals(originalName)) {
                            if (propVal instanceof List<?>) {
                                if (originalName.contains(KEYWORD_OWNER)) {
                                    Object o1 = handleOwner(blockContext, originalName, propVal);
                                    velocityContext.put(remappedName, handlePropValQualifiedName(o1));
                                } else {
                                    velocityContext.put(remappedName, handleValueLists((List<?>) propVal));
                                }
                            } else {
                                if (originalName.contains(KEYWORD_OWNER)) {
                                    Object o1 = handleOwner(blockContext, originalName, propVal);
                                    velocityContext.put(remappedName, handlePropValQualifiedName(o1));
                                } else {
                                    velocityContext.put(remappedName, handlePropValQualifiedName(propVal));
                                }
                            }
                        }
                    });
                }

            });

            blockContext.getConnectedClass().getAppliedStereotypes().forEach(stereotype -> {
                StereotypeMapping stereotypeMapping = mappingWrapper.getStereotypeMappings().get(stereotype.getName());
                if (stereotypeMapping != null) {
                    stereotypeMapping.getModelCommands().forEach((originalName, remappedName) -> {
                        if (originalName.contains(KEYWORD_THIS)) {
                            Object o = handleThis(blockContext, originalName);
                            if (o != null) {
                                velocityContext.put(remappedName, o);
                            }
                        } else if (originalName.contains(KEYWORD_CONNECTION)) {
                            Object o = handleConnection(blockContext, originalName);
                            if (o != null) {
                                velocityContext.put(remappedName, o);
                            }
                        }
                    });
                }
            });

            NameMapping nameMapping = mappingWrapper.getNameMappings().get(blockContext.getConnectedClass().getName());
            if (nameMapping != null) {
                nameMapping.getModelCommands().forEach((originalName, remappedName) -> {
                    if (originalName.contains(KEYWORD_THIS)) {
                        Object o = handleThis(blockContext, originalName);
                        if (o != null) {
                            velocityContext.put(remappedName, o);
                        }
                    } else if (originalName.contains(KEYWORD_CONNECTION)) {
                        Object o = handleConnection(blockContext, originalName);
                        if (o != null) {
                            velocityContext.put(remappedName, o);
                        }
                    }
                });
            }

            blockContext.getLinkedPartContexts().forEach((propName, bcList) -> bcList.forEach(bc -> {
                VelocityContext context = handleBlockContext(bc, alreadyHandled);
                mergeContexts(velocityContext, context);
            }));
        }
        return velocityContext;
    }

    private static Object handleConnection(BlockContext bc, String originalName) {
        String[] split = originalName.split(KEYWORD_SEPARATOR);
        if (split.length == 3 && split[0].equals(KEYWORD_CONNECTION)) {
            return handleConnectedElement(bc, split[1], split[2], new HashSet<>());
        }
        return null;
    }

    private static Object handleConnectedElement(BlockContext bc, String connectedElement, String attributeToGet, Set<BlockContext> alreadyChecked) {
        final Set<Object> connectedElements = new HashSet<>();
        bc.getLinkedPartContexts().forEach((propName, contextList) -> contextList.forEach(context -> {
                    context.getStereotypeToPropsMap().forEach((stereoname, stereoprops) -> {
                        if (getNameFromQualifiedName(stereoname).equals(connectedElement)) {
                            Object o = handleBlockConfig(context, attributeToGet, connectedElement);
                            if (o != null) connectedElements.add(o);
                        }
                    });
                    alreadyChecked.add(context);
                    context.getLinkedPartContexts().forEach((key, value) -> value.forEach(indirectContext -> {
                        if (!alreadyChecked.contains(indirectContext)) {
                            Object o = handleConnectedElement(indirectContext, connectedElement, attributeToGet, alreadyChecked);
                            if (o != null) connectedElements.add(o);
                        }
                    }));

                }
        ));
        if (!connectedElements.isEmpty()) {
            if (connectedElements.size() > 1) {
                // TODO MAE should end up here, find out why not
                connectedElements.forEach(ce -> System.out.println("CONNECTED ELEMENT " + ce));
                System.out.println("COULD NOT UNIQUELY IDENTIFY CONNECTED ELEMENT, RETURNING FIRST ELEMENT!");
            }
            return connectedElements.iterator().next();
        }
        return null;
    }

    private static Object handleThis(BlockContext bc, String originalName) {
        String[] split = originalName.split(KEYWORD_SEPARATOR);
        if (split.length == 3 && split[0].equals(KEYWORD_THIS)) {
            if (split[1].equals(KEYWORD_BLOCK)) {
                return handleBlockConfig(bc, split[2], split[1]);
            }
        }
        return null;
    }

    private static Object handleBlockConfig(BlockContext bc, String attributeToGet, String stereoName) {
        if (attributeToGet.equals(KEYWORD_NAME)) {
            return bc.getConnectedClass().getName();
        } else if (attributeToGet.equals(KEYWORD_PROPNAME)) {
            final Set<Object> matchingAttributes = new HashSet<>();
            bc.getPropertyMap().forEach((propName, propVal) -> {
                String[] split = propName.split(QUALIFIED_NAME_SEPARATOR);
                if (split.length > 2) {
                    String propStereo = split[split.length - 2];
                    if (propStereo.equals(stereoName)) {
                        String[] s = split[0].split(PROPNAME_QUALIFIED_NAME_SEPARATOR);
                        matchingAttributes.add(s[0]);
                    }
                }
            });
            if (!matchingAttributes.isEmpty()) {
                if (matchingAttributes.size() > 1)
                    System.out.println("CANNOT UNIQUELY IDENTIFY ATTRIBUTE, RETURNING FIRST ONE");
                return matchingAttributes.iterator().next();
            }
        } else {
            final Set<Object> matchingAttributes = new HashSet<>();
            bc.getPropertyMap().forEach((propName, propVal) -> {
                String nameFromQualifiedName = getNameFromQualifiedName(propName);
                if (nameFromQualifiedName.equals(attributeToGet)) {
                    matchingAttributes.add(propVal);
                }
            });
            if (!matchingAttributes.isEmpty()) {
                if (matchingAttributes.size() > 1)
                    System.out.println("CANNOT UNIQUELY IDENTIFY ATTRIBUTE, RETURNING FIRST ONE");
                return matchingAttributes.iterator().next();
            }
        }
        return null;
    }

    private static Set<Object> handleOwnerInternal(BlockContext bc, String attributeToGet, Object propVal) {
        Object o = handlePropValGetOwner(propVal);
        final Set<Object> matchingPropVals = new HashSet<>();
        bc.getLinkedPartContexts().forEach((name, contextList) -> contextList.forEach(con -> {
            if (con.getConnectedClass().getName().equals(o)) {
                con.getPropertyMap().forEach((key, value) -> {
                    String nameFromQualifiedName = getNameFromQualifiedName(key);
                    if (nameFromQualifiedName.equals(attributeToGet)) {
                        matchingPropVals.add(value);
                    }
                });

            }
        }));
        return matchingPropVals;
    }

    private static Object handleOwner(final BlockContext bc, final String orginalNameWithOwner, final Object propVal) {
        if (orginalNameWithOwner != null && !orginalNameWithOwner.isEmpty()) {
            if (orginalNameWithOwner.contains(KEYWORD_OWNER)) {
                String[] split = orginalNameWithOwner.split(KEYWORD_SEPARATOR);
                if (split.length == 3 && split[1].equals(KEYWORD_OWNER)) {
                    String getOwnerFor = split[0];
                    String attributeOfOwner = split[2];
                    Set<Object> objects = handleOwnerInternal(bc, attributeOfOwner, propVal);
                    if (!objects.isEmpty()) {
                        if (objects.size() > 1) System.out.println("PROPERTY OF OWNER CANNOT BE MATCHED UNIQUELY");
                        return objects.iterator().next();
                    }
                }
            }
        }
        return null;
    }

    private static Object handlePropValGetOwner(Object propVal) {
        if (propVal instanceof String) {
            if (((String) propVal).contains(QUALIFIED_NAME_SEPARATOR)) {
                String[] split = ((String) propVal).split(QUALIFIED_NAME_SEPARATOR);
                return split[split.length - 2];
            } else {
                return propVal;
            }
        } else {
            return propVal;
        }
    }

    private static Object handlePropValQualifiedName(Object propVal) {
        if (propVal instanceof String) {
            if (((String) propVal).contains(QUALIFIED_NAME_SEPARATOR)) {
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
                return qualifiedName.substring(qualifiedName.lastIndexOf(QUALIFIED_NAME_SEPARATOR) + 2);
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
                String[] split = qualifiedName.split(QUALIFIED_NAME_SEPARATOR);
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
