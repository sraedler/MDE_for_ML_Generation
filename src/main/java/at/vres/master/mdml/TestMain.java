package at.vres.master.mdml;

import at.vres.master.mdml.decomposition.InformationExtractor;
import at.vres.master.mdml.decomposition.MLInformationHolder;
import at.vres.master.mdml.decomposition.ModelDecompositionHandler;
import at.vres.master.mdml.mapping.MappingHandler;
import at.vres.master.mdml.mapping.SimpleJSONInfoHolder;
import at.vres.master.mdml.tbcg.VelocityTemplateHandler;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;

import java.io.StringWriter;
import java.io.Writer;
import java.security.KeyException;
import java.util.*;

public class TestMain {
    private static final String TEST_MODEL = "SysMLModels/UC1_Weather/UC1_Weather.uml";
    private static final String TEST_TEMPLATE_PATH = "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates";
    private static final String ENCODING = "UTF-8";
    private static final String STATE_MACHINE_NAME = "StateMachine1";
    private static final Map<String, String> stereoTemplateMapping = new HashMap<>(Map.of(
            "CSV", "csv_load.vm",
            "Regression","regression.vm",
            "DataFrame_Merge", "dataframe_merge.vm",
            "DateConversion", "date_conversion.vm",
            "MeanAbsoluteError","mae.vm",
            "Train_Test_Split","train_test_split.vm",
            "Predict","predict.vm"
    ));


    public static void main(String[] args) {
        //System.out.println(testInternalEngineLoad());
        testV2(TEST_MODEL);
    }

    public static String testInternalEngineLoad() {
        Map<String, MLInformationHolder> stringMLInformationHolderMap = ModelDecompositionHandler.doExtraction(TEST_MODEL);
        //ModelDecompositionHandler.prettyPrintMLInformationHolderMap(stringMLInformationHolderMap);
        //ModelDecompositionHandler.prettyPrintMLInformationHolderList(ModelDecompositionHandler.getOrderedList());
        VelocityTemplateHandler vth = new VelocityTemplateHandler();
        vth.setModelInformation(stringMLInformationHolderMap);
        vth.initInternalEngine(TEST_TEMPLATE_PATH);
        final StringBuilder sb = new StringBuilder();
        List<MLInformationHolder> orderedList = ModelDecompositionHandler.getOrderedList();
        orderedList.forEach(state -> stereoTemplateMapping.forEach((stereo, template) -> {
            if(state.getStereotypes().containsKey(stereo)) {
                sb.append(vth.createContextInternalAndMerge(state, template, ENCODING)).append("\n");
            }
        }));
        vth.getContexts().forEach((context, s) -> System.out.println(s + ": " + Arrays.toString(context.getKeys())));
        SimpleJSONInfoHolder simpleJSONInfoHolder = MappingHandler.readJSONSimple("mappings\\json_v2.json");
        String templateFolder = simpleJSONInfoHolder.getTemplateFolder();
        System.out.println("templateFolder = " + templateFolder);
        simpleJSONInfoHolder.getMappings().forEach(m -> {
            System.out.println("\t" + m.getTemplate());
            System.out.println("\t" + m.getMlConnection());
            m.getParameters().forEach((key, value) -> System.out.println("\t\t" + key + ": " + value));
        });
        final String templateFolder1 = simpleJSONInfoHolder.getTemplateFolder();
        vth.initExternalEngine(templateFolder1);
        simpleJSONInfoHolder.getMappings().forEach(m -> {
            Map<String, String> stringStringMap = MappingHandler.resolveMappingFromModel(simpleJSONInfoHolder, stringMLInformationHolderMap);
            stringStringMap.forEach((key, value) -> System.out.println("key = " + key + ", value = " + value));
            try {
                Writer contextExternalAndMerge = vth.createContextExternalAndMerge(stringStringMap, m.getTemplate(), ENCODING, templateFolder1, new StringWriter());
                sb.append((contextExternalAndMerge.toString()));
            } catch (KeyException e) {
                e.printStackTrace();
            }
        });
        return sb.toString();
    }

    public static void testV2(String modelPath) {
        Model model = InformationExtractor.getModel(modelPath, true);
        Map<Class, Map<String, String>> classMapMap = InformationExtractor.doExtraction(model, STATE_MACHINE_NAME);
        classMapMap.forEach((key, value) -> {
            System.out.println(key.getName());
            value.forEach((akey, avalue) -> {
                System.out.println("\t" + akey + " -> " + avalue);
            });
        });
    }


}
