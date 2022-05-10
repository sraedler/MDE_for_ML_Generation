package at.vres.master.mdml.model;

import org.eclipse.uml2.uml.Class;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlockContext {
    private Class connectedClass;
    private String qualifiedName;
    private Map<String, List<BlockContext>> linkedPartContexts;
    private Map<String, Object> propertyMap;
    private Map<String, List<String>> stereotypeToPropsMap;
    private Integer executionOrder;

    private BlockContext() {
    }

    public void addLinkedBlockContext(String qualifiedName, BlockContext blockContext) {
        List<BlockContext> blockContexts = linkedPartContexts.get(qualifiedName);
        if (blockContexts != null) blockContexts.add(blockContext);
        else linkedPartContexts.put(qualifiedName, new LinkedList<>(List.of(blockContext)));
    }

    public void addStereotypeAttributeMapping(String stereotypeName, String attributeName) {
        List<String> strings = stereotypeToPropsMap.get(stereotypeName);
        if (strings != null) strings.add(attributeName);
        else stereotypeToPropsMap.put(stereotypeName, new LinkedList<>(List.of(attributeName)));
    }

    public void addStereotypeAttributeListMapping(String stereotypeName, List<String> attributeNames) {
        List<String> strings = stereotypeToPropsMap.get(stereotypeName);
        if (strings != null) strings.addAll(attributeNames);
        else stereotypeToPropsMap.put(stereotypeName, new LinkedList<>(attributeNames));
    }


    public Map<String, List<String>> getStereotypeToPropsMap() {
        return stereotypeToPropsMap;
    }

    public void setStereotypeToPropsMap(Map<String, List<String>> stereotypeToPropsMap) {
        this.stereotypeToPropsMap = stereotypeToPropsMap;
    }

    public BlockContext(Class connectedClass) {
        this.connectedClass = connectedClass;
        this.qualifiedName = connectedClass.getQualifiedName();
        linkedPartContexts = new HashMap<>();
        propertyMap = new HashMap<>();
        stereotypeToPropsMap = new HashMap<>();
        executionOrder = -1;
    }

    public Class getConnectedClass() {
        return connectedClass;
    }

    public void setConnectedClass(Class connectedClass) {
        this.connectedClass = connectedClass;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Map<String, List<BlockContext>> getLinkedPartContexts() {
        return linkedPartContexts;
    }

    public void setLinkedPartContexts(Map<String, List<BlockContext>> linkedPartContexts) {
        this.linkedPartContexts = linkedPartContexts;
    }

    public String getBlockContextQualifiedName(BlockContext value) {
        return value != null ? value.getQualifiedName() : "null";
    }

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }

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
