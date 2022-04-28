package at.vres.master.mdml.mapping;

import at.vres.master.mdml.mapping.impl.PropertyParameterValue;
import at.vres.master.mdml.mapping.impl.StringParameterValue;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONMappingDeserializer extends StdDeserializer<JSONInformationHolderV2> {
    private static final String SPLIT_SYMBOL = "&&";

    public JSONMappingDeserializer() {
        this(null);
    }

    public JSONMappingDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JSONInformationHolderV2 deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        String templateFolder = node.get("templateFolderPath").asText();
        String mlConnection = node.get("mlConnection").asText();
        Map<String, String> paramMap = new HashMap<>();
        JsonNode mappings = node.get("mappings");
        if (mappings.isArray()) {
            System.out.println("MAPPINGS DESERIALIZATION");
            mappings.forEach(m -> {
                String tempName = m.get("template").asText();
                final Map<String, String> pMap = new HashMap<>();
                JsonNode parameters = m.get("parameters");
                if (parameters.isArray()) {
                    System.out.println("PARAMETER DESERIALIZATION");
                    parameters.forEach(par -> {
                        pMap.put(par.get(0).asText(), par.get(1).asText());
                    });
                }
            });
        }

        return null;
    }

    private static ITemplateParameter createParam(String name, String valString) {
        String[] split = valString.split(SPLIT_SYMBOL);
        JSONTemplateParameter jsonTemplateParameter = new JSONTemplateParameter();
        jsonTemplateParameter.setName(name);
        jsonTemplateParameter.setValueString(valString);
        switch (split.length) {
            case 1:
                StringParameterValue val = new StringParameterValue();
                val.setParamValue(valString);
                jsonTemplateParameter.setParameterValue(val);
                break;
            case 2:
                PropertyParameterValue pval = new PropertyParameterValue();
                break;
        }
        return jsonTemplateParameter;
    }

}
