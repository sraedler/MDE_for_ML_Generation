package at.vres.master.mdml;

import at.vres.master.mdml.decomposition.MLInformationHolder;
import at.vres.master.mdml.decomposition.ModelDecompositionHandler;
import at.vres.master.mdml.mapping.JSONInformationHolder;
import at.vres.master.mdml.mapping.MappingHandler;
import at.vres.master.mdml.tbcg.VelocityTest;
import org.apache.velocity.VelocityContext;

import java.util.Map;

public class TestMain {


    public static void main(String[] args) {
        Map<String, MLInformationHolder> stringMLInformationHolderMap = ModelDecompositionHandler.doExtraction();
        VelocityTest.generateFromExtractedInformation(stringMLInformationHolderMap, "test.vm");
    }


}
