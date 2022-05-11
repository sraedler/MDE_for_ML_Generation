package at.vres.master.mdml.model;

import org.apache.velocity.VelocityContext;

import java.util.LinkedList;
import java.util.List;

public class ContextAndTemplatesPair {
    private VelocityContext context;
    private List<String> templateNames;

    public ContextAndTemplatesPair(VelocityContext context, List<String> templateNames) {
        this.context = context;
        this.templateNames = templateNames;
    }

    public ContextAndTemplatesPair(VelocityContext context) {
        this.context = context;
        this.templateNames = new LinkedList<>();
    }

    public void addTemplateName(String templateName) {
        templateNames.add(templateName);
    }

    public void addTemplateNames(List<String> templateNames) {
        this.templateNames.addAll(templateNames);
    }

    public VelocityContext getContext() {
        return context;
    }

    public void setContext(VelocityContext context) {
        this.context = context;
    }

    public List<String> getTemplateNames() {
        return templateNames;
    }

    public void setTemplateNames(List<String> templateNames) {
        this.templateNames = templateNames;
    }
}
