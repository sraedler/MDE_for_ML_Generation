package at.vres.master.mdml.tbcg;

import at.vres.master.mdml.decomposition.MLInformationHolder;
import at.vres.master.mdml.mapping.JSONInformationHolder;
import at.vres.master.mdml.mapping.MappingHandler;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class VelocityTest {
    private static final String VELOCITY_TEMPLATE_PATH_KEY = "file.resource.loader.path";

    private static Properties getDefaultProperties() {
        Properties p = new Properties();
        p.setProperty(VELOCITY_TEMPLATE_PATH_KEY, "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates");
        return p;
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
            System.out.println("TEST: \n" + sw);
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

    public static void velTestRun(String templateName) {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates");
        VelocityEngine ve = new VelocityEngine();
        ve.init(p);

        VelocityContext context = new VelocityContext();
        context.put("df", "df");
        context.put("y", "weather");
        context.put("train_size", 0.7);
        context.put("features", new String[]{"\"precipitation\", \"temp_max\", \"temp_min\", \"wind\""});

        try (StringWriter sw = new StringWriter()) {
            ve.mergeTemplate(templateName, "UTF-8", context, sw);
            System.out.println("TEST: \n" + sw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
