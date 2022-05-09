package at.vres.master.mdml.tbcg;

import at.vres.master.mdml.decomposition.MLInformationHolder;
import at.vres.master.mdml.mapping.JSONInformationHolder;
import at.vres.master.mdml.mapping.MappingHandler;
import at.vres.master.mdml.utils.ContextResolver;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

import java.io.*;
import java.security.KeyException;
import java.util.*;

public class VelocityTemplateHandler {
    private static final String VELOCITY_TEMPLATE_PATH_KEY = "file.resource.loader.path";
    private final List<String> templateNames = new LinkedList<>();
    private VelocityEngine internalEngine;
    private final Map<String, VelocityEngine> externalEngines = new HashMap<>();
    private final Map<Context, String> contexts = new HashMap<>();
    private Map<String, MLInformationHolder> modelInformation = new HashMap<>();

    public void initInternalEngine(String internalTemplateFolderPath) {
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty(VELOCITY_TEMPLATE_PATH_KEY, internalTemplateFolderPath);
        ve.init(p);
        internalEngine = ve;
    }

    public void addMLInformationToContext(MLInformationHolder data, Context context) {
        data.getParts().forEach((key, value) -> {
            context.put(key, ContextResolver.resolveVariableFromObject(value));
            String qualifiedName = ((Class) value).getQualifiedName();
            if (modelInformation.containsKey(qualifiedName)) {
                MLInformationHolder mlInformationHolder = modelInformation.get(qualifiedName);
                addMLInformationToContext(mlInformationHolder, context);
            }
        });
        data.getProperties().forEach((key, value) -> {
            context.put(key, ContextResolver.resolveVariableFromObject(value));
            System.out.println("PROP ADDING FOR: " + key + " WITH VALUE: " + value);
            if (value instanceof Property) {
                if(((Property)value).getAppliedStereotypes().stream().anyMatch(s -> s.getName().equals("ML_Attribute_Input"))) {
                    String qualifiedName = ((Property) value).getQualifiedName();
                    System.out.println("\tSTEREO MATCH: " + qualifiedName);
                    MLInformationHolder mlInformationHolder = modelInformation.get(qualifiedName);
                    if(mlInformationHolder != null) {
                        System.out.println("AAAAA" + key + ": " + mlInformationHolder.getQualifiedName());
                        addMLInformationToContext(mlInformationHolder, context);
                    }
                }
            }
        });
        data.getStereotypes().forEach((stKey, stVal) -> stVal.forEach((valKey, Valvalue) -> context.put(valKey, ContextResolver.resolveVariableFromObject(Valvalue))));
    }

    public String createContextInternalAndMerge(MLInformationHolder data, String templateName, String encoding) {
        Writer writer = new StringWriter();
        VelocityContext context = new VelocityContext();
        addMLInformationToContext(data, context);
        contexts.put(context, templateName);
        if (internalEngine != null) internalEngine.mergeTemplate(templateName, encoding, context, writer);
        else throw new NullPointerException("The internal engine has not been initialized!");
        return writer.toString();
    }

    public Writer createContextExternalAndMerge(Map<String, String> data, String templateName, String encoding, String templateFolder, Writer writer) throws KeyException {
        VelocityContext context = new VelocityContext();
        data.forEach(context::put);
        contexts.put(context, templateName);
        if (externalEngines.containsKey(templateFolder))
            externalEngines.get(templateFolder).mergeTemplate(templateName, encoding, context, writer);
        else throw new KeyException("No VelocityEngine initialized for the given template folder path!");
        return writer;
    }

    public void clearExternalEngines() {
        externalEngines.clear();
    }

    public boolean removeExternalEngine(String externalTemplateFolderPath) {
        return externalEngines.remove(externalTemplateFolderPath) != null;
    }

    public void initExternalEngine(String externalTemplateFolderPath) {
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty(VELOCITY_TEMPLATE_PATH_KEY, externalTemplateFolderPath);
        ve.init(p);
        externalEngines.put(externalTemplateFolderPath, ve);
    }

    public void initExternalEngines(List<String> externalTemplateFolderPaths) {
        externalTemplateFolderPaths.forEach(this::initExternalEngine);
    }

    private static Properties getDefaultProperties() {
        Properties p = new Properties();
        p.setProperty(VELOCITY_TEMPLATE_PATH_KEY, "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates");
        return p;
    }

    public List<String> getTemplateNames() {
        return templateNames;
    }

    public void addTemplate(String templateName) {
        templateNames.add(templateName);
    }

    public void loadTemplates() {
        templateNames.forEach(temp -> {

        });
    }

    public static String generateFromExtractedInformation(Map<String, MLInformationHolder> map, String templateName) {
        VelocityEngine ve = new VelocityEngine();
        ve.init(getDefaultProperties());
        VelocityContext context = new VelocityContext();
        map.forEach((key, value) -> {
            value.getParts().forEach(context::put);
            value.getProperties().forEach(context::put);
            value.getStereotypes().forEach((stKey, stVal) -> stVal.forEach(context::put));
        });

        String result = "";
        try (StringWriter sw = new StringWriter()) {
            ve.mergeTemplate(templateName, "UTF-8", context, sw);
            final String absPath = "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates\\" + templateName;
            try {
                RuntimeInstance ri = new RuntimeInstance();
                SimpleNode node =  ri.parse(new FileReader(absPath), ve.getTemplate(templateName));
                TemplateVisitor tv = new TemplateVisitor();
                Object visit = tv.visit(node, null);
                System.out.println("visit = " + visit);
            } catch (ParseException | FileNotFoundException e) {
                e.printStackTrace();
            }
            result = sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String generateFromJSON(String jsonPath) {
        JSONInformationHolder jih = MappingHandler.readJSON(jsonPath);
        Properties properties = new Properties();
        properties.put(VELOCITY_TEMPLATE_PATH_KEY, jih.getTemplateFolder());
        return generateFromObject(jih.getTemplate(), jih.getParameters(), properties);
    }

    public static String generateFromObject(String templateName, Map<String, Object> parameters, Properties properties) {
        VelocityEngine ve = new VelocityEngine();
        ve.init(properties);
        VelocityContext context = new VelocityContext();
        parameters.forEach(context::put);
        String result = "";
        try (StringWriter sw = new StringWriter()) {
            ve.mergeTemplate(templateName, "UTF-8", context, sw);
            System.out.println("TEST: \n" + sw);
            result = sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public VelocityEngine getInternalEngine() {
        return internalEngine;
    }

    public Map<String, VelocityEngine> getExternalEngines() {
        return externalEngines;
    }

    public Map<Context, String> getContexts() {
        return contexts;
    }

    public Map<String, MLInformationHolder> getModelInformation() {
        return modelInformation;
    }

    public void setModelInformation(Map<String, MLInformationHolder> modelInformation) {
        this.modelInformation = modelInformation;
    }
}
