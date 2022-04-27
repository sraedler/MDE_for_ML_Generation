package at.vres.master.mdml;

import at.vres.master.mdml.decomposition.MLInformationHolder;
import at.vres.master.mdml.decomposition.ModelDecompositionHandler;
import at.vres.master.mdml.tbcg.VelocityTemplateHandler;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestMain {
    private static final String TEST_MODEL = "SysMLModels/UC1_Weather/UC1_Weather.uml";
    private static final String TEST_TEMPLATE_PATH = "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates";
    private static final String ENCODING = "UTF-8";
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
        /*
        Map<String, MLInformationHolder> stringMLInformationHolderMap = ModelDecompositionHandler.doExtraction(TEST_MODEL);
        List<MLInformationHolder> list = ModelDecompositionHandler.getOrderedList();
        List<String> vmList = new LinkedList<>(List.of(
                "csv_load.vm",
                "dataframe_merge.vm",
                "date_conversion.vm",
                "mae.vm",
                "predict.vm",
                "regression.vm",
                "train_test_split.vm"
        ));
        final StringBuilder sb = new StringBuilder();
        vmList.forEach(template -> sb.append(VelocityTemplateHandler.generateFromExtractedInformation(stringMLInformationHolderMap, template)));
        System.out.println("sb = " + sb);
        ModelDecompositionHandler.prettyPrintMLInformationHolderMap(stringMLInformationHolderMap);
         */
        //String s = VelocityTest.generateFromExtractedInformation(stringMLInformationHolderMap, "test.vm");
        /*
        if(!s.isBlank()) {
           DocOnceHandler.ipynbGenerate(s, "dotFiles/test.aipynb");
        }
         */
        //VelocityTest.velTestRun("test.vm");
        //VelocityTest.generateFromJSON("mappings/test.json");
        System.out.println(testInternalEngineLoad());
    }

    public static String testInternalEngineLoad() {
        Map<String, MLInformationHolder> stringMLInformationHolderMap = ModelDecompositionHandler.doExtraction(TEST_MODEL);
        VelocityTemplateHandler vth = new VelocityTemplateHandler();
        vth.initInternalEngine(TEST_TEMPLATE_PATH);
        final StringBuilder sb = new StringBuilder();
        List<MLInformationHolder> orderedList = ModelDecompositionHandler.getOrderedList();
        orderedList.forEach(state -> stereoTemplateMapping.forEach((stereo, template) -> {
            if(state.getStereotypes().containsKey(stereo)) {
                sb.append(vth.createContextInternalAndMerge(state, template, ENCODING)).append("\n");
            }
        }));
        return sb.toString();
    }


}
