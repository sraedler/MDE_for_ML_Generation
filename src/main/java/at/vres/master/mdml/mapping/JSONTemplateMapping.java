package at.vres.master.mdml.mapping;

import at.vres.master.mdml.model.ML;

import java.util.List;

public class JSONTemplateMapping implements IMapping{
    private String templateName;
    private ML mlConnection;
    private List<ITemplateParameter> parameters;

    @Override
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public ML getMlConnection() {
        return mlConnection;
    }

    public void setMlConnection(ML mlConnection) {
        this.mlConnection = mlConnection;
    }

    @Override
    public List<ITemplateParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ITemplateParameter> parameters) {
        this.parameters = parameters;
    }
}
