package at.vres.master.mdml.mapping;

import at.vres.master.mdml.model.ML;

import java.util.List;

public interface IMapping {
    String getTemplateName();
    List<ITemplateParameter> getParameters();
    ML getMlConnection();
}
