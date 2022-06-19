package at.vres.master.mdml;

import at.vres.master.mdml.decomposition.InformationExtractor;
import at.vres.master.mdml.formatters.EmptyLineFormatter;
import at.vres.master.mdml.mapping.MappingHandler;
import at.vres.master.mdml.mapping.MappingWrapper;
import at.vres.master.mdml.model.BlockContext;
import at.vres.master.mdml.formatters.ImportFormatter;
import at.vres.master.mdml.tbcg.TemplateHandler;
import org.eclipse.uml2.uml.Class;

import java.util.*;

/**
 * Main class for quick prototyping.
 */
public class TestMain {
    private static final String TEST_MODEL = "SysMLModels/UC1_Weather/UC1_Weather.uml";
    private static final String UPDATED_TEST_MODEL = "SysMLModels/New_UC1_Weather/UC1_Weather.uml";
    private static final String WORKSPACE_PROFILE_TEST_MODEL = "C:\\Users\\rup\\Documents\\MASTER\\MLModels\\NewVer_04_05_2022\\ML_Modelling\\UC1_Weather\\UC1_Weather.uml";
    private static final String WORKSPACE_UPDATED_MODEL = "C:\\Users\\rup\\Documents\\MASTER\\MasterModels\\ML_Modelling\\UC1_Weather\\UC1_Weather.uml";
    private static final String TEST_TEMPLATE_PATH = "templates";
    private static final String JSON_CONFIG_PATH = "mappings\\config.json";
    private static final String STATE_MACHINE_NAME = "StateMachine1";
    private static final String UPDATED_STATE_MACHINE_NAME = "Learning Workflow";


    /**
     * Main method
     *
     * @param args Arguments for main as String array
     */
    public static void main(String[] args) {
        testContextVariant(UPDATED_TEST_MODEL, UPDATED_STATE_MACHINE_NAME, JSON_CONFIG_PATH);
    }

    /**
     * Helper method for testing the context variant of the ModelDrivenML framework (is the method that worked the best)
     *
     * @param modelPath        The path to the UML file for the model
     * @param stateMachineName The name of the state machine that represents the ML workflow
     * @param jsonPath         The path to the mapping JSON-file
     */
    public static void testContextVariant(String modelPath, String stateMachineName, String jsonPath) {
        InformationExtractor ie = new InformationExtractor(modelPath);
        Map<Class, BlockContext> contextsForStateMachine = ie.getContextsForStateMachine(stateMachineName);
        contextsForStateMachine.forEach((key, val) -> System.out.println(val.toString()));
        MappingWrapper mappingWrapper = MappingHandler.readJSONV2(jsonPath);
        TemplateHandler th = new TemplateHandler(contextsForStateMachine, mappingWrapper, TEST_TEMPLATE_PATH, ie.getModelName());
        String execute = th.execute();
        String format = ImportFormatter.format(execute);
        String format1 = EmptyLineFormatter.format(format);
        System.out.println("\nformat = \n" + format1);
    }

}
