package at.vres.master.mdml;

import at.vres.master.mdml.decomposition.MLInformationHolder;
import at.vres.master.mdml.decomposition.ModelDecompositionHandler;
import at.vres.master.mdml.tbcg.VelocityTemplateHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestMain {
    private static final String TEST_MODEL = "SysMLModels/UC1_Weather/UC1_Weather.uml";


    public static void main(String[] args) {
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
        //String s = VelocityTest.generateFromExtractedInformation(stringMLInformationHolderMap, "test.vm");
        /*
        if(!s.isBlank()) {
           DocOnceHandler.ipynbGenerate(s, "dotFiles/test.aipynb");
        }
         */
        //VelocityTest.velTestRun("test.vm");
        //VelocityTest.generateFromJSON("mappings/test.json");
    }


}
