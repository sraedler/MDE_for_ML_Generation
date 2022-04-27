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
        System.out.println(testInternalEngineLoad());
    }

    public static String testInternalEngineLoad() {
        Map<String, MLInformationHolder> stringMLInformationHolderMap = ModelDecompositionHandler.doExtraction(TEST_MODEL);
        ModelDecompositionHandler.prettyPrintMLInformationHolderMap(stringMLInformationHolderMap);
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
