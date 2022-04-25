package at.vres.master.mdml;

import at.vres.master.mdml.decomposition.ModelDecompositionHandler;
import at.vres.master.mdml.mapping.JSONInformationHolder;
import at.vres.master.mdml.mapping.MappingHandler;
import at.vres.master.mdml.tbcg.VelocityTest;

public class TestMain {


    public static void main(String[] args) {
        VelocityTest.generateFromJSON("mappings/test.json");
    }


}
