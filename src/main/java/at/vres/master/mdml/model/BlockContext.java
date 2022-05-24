package at.vres.master.mdml.model;

import org.eclipse.uml2.uml.Class;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class for holding the information extracted from the model in a way that makes it easier to merge with the templates
 */
public class BlockContext {
    private Class connectedClass;
    private String qualifiedName;
    private Map<String, List<BlockContext>> linkedPartContexts;
    private Map<String, Object> propertyMap;
    private Map<String, List<String>> stereotypeToPropsMap;
    private Integer executionOrder;

    public BlockContext(Class connectedClass) {
        this.connectedClass = connectedClass;
        this.qualifiedName = connectedClass.getQualifiedName();
        linkedPartContexts = new HashMap<>();
        propertyMap = new HashMap<>();
        stereotypeToPropsMap = new HashMap<>();
        executionOrder = -1;
    }

    /**
     * Add a block context that is linked to this one to the linkedPartContexts list
     *
     * @param qualifiedName The qualified name of the Class the linked BlockContext is connected to
     * @param blockContext  The BlockContext to add to the linkedBlockContexts
     */
    public void addLinkedBlockContext(String qualifiedName, BlockContext blockContext) {
        List<BlockContext> blockContexts = linkedPartContexts.get(qualifiedName);
        if (blockContexts != null) blockContexts.add(blockContext);
        else linkedPartContexts.put(qualifiedName, new LinkedList<>(List.of(blockContext)));
    }

    /**
     * Add a mapping for a stereotype attribute to the context
     *
     * @param stereotypeName The name of the stereotype to add the mapping for
     * @param attributeName  The name of the attribute of the stereotype to add to the mapping
     */
    public void addStereotypeAttributeMapping(String stereotypeName, String attributeName) {
        List<String> strings = stereotypeToPropsMap.get(stereotypeName);
        if (strings != null) strings.add(attributeName);
        else stereotypeToPropsMap.put(stereotypeName, new LinkedList<>(List.of(attributeName)));
    }

    /**
     * Add a list of mappings for stereotype attributes to the context
     *
     * @param stereotypeName The name of the stereotype to add the mappings for
     * @param attributeNames The names of the attributes to add to the mapping
     */
    public void addStereotypeAttributeListMapping(String stereotypeName, List<String> attributeNames) {
        List<String> strings = stereotypeToPropsMap.get(stereotypeName);
        if (strings != null) strings.addAll(attributeNames);
        else stereotypeToPropsMap.put(stereotypeName, new LinkedList<>(attributeNames));
    }

    /**
     * Get the mapping of stereotypes to their attributes
     *
     * @return The mapping of stereotypes to their attributes
     */
    public Map<String, List<String>> getStereotypeToPropsMap() {
        return stereotypeToPropsMap;
    }

    /**
     * Set the mapping between stereotypes and their attributes
     *
     * @param stereotypeToPropsMap The mapping to set for stereotypes and their attributes
     */
    public void setStereotypeToPropsMap(Map<String, List<String>> stereotypeToPropsMap) {
        this.stereotypeToPropsMap = stereotypeToPropsMap;
    }

    /**
     * Get the UML Class connected to this BlockContext
     *
     * @return The UML Class connected to this BlockContext
     */
    public Class getConnectedClass() {
        return connectedClass;
    }

    /**
     * Set the UML Class connected to this BlockContext
     *
     * @param connectedClass The UML Class to set as the connected Class for this BlockContext
     */
    public void setConnectedClass(Class connectedClass) {
        this.connectedClass = connectedClass;
    }

    /**
     * Get the qualified name of the BlockContext (same as the qualifiedName of the UML class connected to this BlockContext)
     *
     * @return The qualified name of the BlockContext
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Set the qualified name of the BlockContext (same as the qualifiedName of the UML class connected to this BlockContext)
     *
     * @param qualifiedName The qualified name to set for this BlockContext
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * Get the map of property names of this BlockContext and the BlockContexts that are linked to those property names
     *
     * @return The Map containing as key the property names of the connected UML class and as value a list of the BlockContexts connected to those keys
     */
    public Map<String, List<BlockContext>> getLinkedPartContexts() {
        return linkedPartContexts;
    }

    /**
     * Set the map of property names of this BlockContext and the BlockContexts that are linked to those property names
     *
     * @param linkedPartContexts The Map of the property names and the linked BlockContexts
     */
    public void setLinkedPartContexts(Map<String, List<BlockContext>> linkedPartContexts) {
        this.linkedPartContexts = linkedPartContexts;
    }

    /**
     * Get the qualifiedName of a BlockContext (null-safe)
     *
     * @param value The BlockContext to get the qualified name for
     * @return The qualifiedName or a String "null" if the value parameter is null
     */
    public String getBlockContextQualifiedName(BlockContext value) {
        return value != null ? value.getQualifiedName() : "null";
    }

    /**
     * Get the mapping between the qualified names of the properties and their values
     *
     * @return The Map containing the qualified names of the properties as key and the respective values as the values
     */
    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    /**
     * Set the mapping between the qualified names of the properties and their values
     *
     * @param propertyMap The Map with the qualified names of the properties and their values to set for the BlockContext
     */
    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }

    /**
     * Return the execution order of the BlockContext (will determine the order of the code generation, lower integer value == earlier execution)
     *
     * @return The execution order value of the BlockContext
     */
    public Integer getExecutionOrder() {
        return executionOrder;
    }

    /**
     * Set the execution order value of the BlockContext
     *
     * @param executionOrder The integer value for the execution order to set
     */
    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{ Context for ").append(qualifiedName).append(" :\n");
        sb.append("\tExecution Order = ").append(executionOrder).append("\n");
        sb.append("\tProperties:\n");
        propertyMap.forEach((key, value) ->
                sb.append("\t\t").append(key).append(" = ").append(value).append("\n"));
        sb.append("\tConnected Block Contexts:\n");
        linkedPartContexts.forEach((key, value) -> {
            sb.append("\t\t").append(key).append(" = ").append("[ ");
            value.forEach(val -> sb.append(val.getQualifiedName()).append(", "));
        });
        sb.append("]\n");
        sb.append("\tStereotype Mappings:\n");
        stereotypeToPropsMap.forEach((key, value) ->
                sb.append("\t\t").append(key).append(" = ").append(value).append("\n"));
        sb.append("}");
        return sb.toString();
    }
}
