package at.vres.master.mdml.decomposition;

import at.vres.master.mdml.model.BlockContext;
import org.eclipse.uml2.uml.Class;

import java.util.Comparator;
import java.util.Map;

/**
 * Comparator for UML Class to BlockContext mappings which will sort based on the BlockContext execution order
 */
public class ContextComparator implements Comparator<Map.Entry<Class, BlockContext>> {

    @Override
    public int compare(Map.Entry<Class, BlockContext> o1, Map.Entry<Class, BlockContext> o2) {
        if (o1.getValue().getExecutionOrder().equals(-1)) {
            return 1;
        }
        if (o2.getValue().getExecutionOrder().equals(-1)) {
            return -1;
        }
        return o1.getValue().getExecutionOrder() - o2.getValue().getExecutionOrder();
    }
}
