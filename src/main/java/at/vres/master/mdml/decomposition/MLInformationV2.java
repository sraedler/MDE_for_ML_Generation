package at.vres.master.mdml.decomposition;

import at.vres.master.mdml.model.IStereotype;

import java.util.LinkedList;
import java.util.List;

public class MLInformationV2 {
    private String name;
    private String qualifiedName;
    private final List<IStereotype> stereotypes = new LinkedList<>();
    
    public Boolean addStereotype(IStereotype stereotype) {
        return stereotypes.add(stereotype);
    }

    public IStereotype getStereotypeByName(String name) {
        return stereotypes.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public List<IStereotype> getStereotypes() {
        return stereotypes;
    }
}
