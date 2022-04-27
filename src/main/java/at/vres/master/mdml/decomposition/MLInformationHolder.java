package at.vres.master.mdml.decomposition;

import MLModel.ML;

import java.util.HashMap;
import java.util.Map;

public class MLInformationHolder {
	private String qualifiedName;
	private String name;
	private Map<String, Map<String, Object>> stereotypes = new HashMap<>();
	private Map<String, Object> properties = new HashMap<>();
	private Map<String, Object> parts = new HashMap<>();
	private ML connectedElement;

	public MLInformationHolder() {
	}

	public MLInformationHolder(String qualifiedName, String name) {
		super();
		this.qualifiedName = qualifiedName;
		this.name = name;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Map<String, Object>> getStereotypes() {
		return stereotypes;
	}

	public void setStereotypes(Map<String, Map<String, Object>> stereotypes) {
		this.stereotypes = stereotypes;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getParts() {
		return parts;
	}

	public void setParts(Map<String, Object> parts) {
		this.parts = parts;
	}

	public ML getConnectedElement() {
		return connectedElement;
	}

	public void setConnectedElement(ML connectedElement) {
		this.connectedElement = connectedElement;
	}
}
