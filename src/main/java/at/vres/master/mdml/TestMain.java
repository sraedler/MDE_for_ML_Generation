package at.vres.master.mdml;

import at.vres.master.mdml.decomposition.InformationExtractor;
import at.vres.master.mdml.mapping.MappingHandler;
import at.vres.master.mdml.mapping.MappingWrapper;
import at.vres.master.mdml.model.BlockContext;
import at.vres.master.mdml.tbcg.TemplateHandler;
import org.eclipse.uml2.uml.Class;

import java.util.*;

public class TestMain {
    private static final String TEST_MODEL = "SysMLModels/UC1_Weather/UC1_Weather.uml";
    private static final String WORKSPACE_PROFILE_TEST_MODEL = "C:\\Users\\rup\\Documents\\MASTER\\MLModels\\NewVer_04_05_2022\\ML_Modelling\\UC1_Weather\\UC1_Weather.uml";
    private static final String TEST_TEMPLATE_PATH = "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates";
    private static final String JSON_CONFIG_PATH = "mappings\\config_v2.json";
    private static final String STATE_MACHINE_NAME = "StateMachine1";


    public static void main(String[] args) {
        testContextVariant(WORKSPACE_PROFILE_TEST_MODEL, STATE_MACHINE_NAME, JSON_CONFIG_PATH);
    }

    public static void testContextVariant(String modelPath, String stateMachineName, String jsonPath) {
        InformationExtractor ie = new InformationExtractor(modelPath);
        Map<Class, BlockContext> contextsForStateMachine = ie.getContextsForStateMachine(stateMachineName);
        contextsForStateMachine.forEach((key, val) -> System.out.println(val.toString()));
        MappingWrapper mappingWrapper = MappingHandler.readJSONV2(jsonPath);
        TemplateHandler th = new TemplateHandler(contextsForStateMachine, mappingWrapper, TEST_TEMPLATE_PATH);
        String execute = th.execute();
        System.out.println("\nexecute = \n" + execute);
    }

}
