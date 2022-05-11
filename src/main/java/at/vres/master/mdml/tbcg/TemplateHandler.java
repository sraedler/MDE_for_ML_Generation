package at.vres.master.mdml.tbcg;

import at.vres.master.mdml.mapping.MappingWrapper;
import at.vres.master.mdml.mapping.NameMapping;
import at.vres.master.mdml.mapping.StereotypeMapping;
import at.vres.master.mdml.model.BlockContext;
import at.vres.master.mdml.model.ContextAndTemplatesPair;
import at.vres.master.mdml.model.StereotypeNamePair;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.uml2.uml.Class;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class TemplateHandler {
    private static final String VELOCITY_TEMPLATE_PATH_KEY = "file.resource.loader.path";
    private static final String ENCODING = "UTF-8";
    private final Map<Class, BlockContext> contexts;
    private final MappingWrapper mappingWrapper;
    private final String templatePath;

    public TemplateHandler(Map<Class, BlockContext> contexts, MappingWrapper mappingWrapper, String templatePath) {
        this.contexts = contexts;
        this.mappingWrapper = mappingWrapper;
        this.templatePath = templatePath;
    }

    public String execute() {
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        final StringBuilder sb = new StringBuilder();
        p.setProperty(VELOCITY_TEMPLATE_PATH_KEY, templatePath);
        ve.init(p);
        contexts.forEach((key, value) -> {
            VelocityContext context = handleBlockContext(value);
            key.getAppliedStereotypes().forEach(stereo -> {
                StereotypeMapping stereotypeMapping = mappingWrapper.getStereotypeMappings().get(stereo.getName());
                if (stereotypeMapping != null) {
                    try (StringWriter writer = new StringWriter()) {
                        ve.mergeTemplate(stereotypeMapping.getTemplate(), ENCODING, context, writer);
                        sb.append(writer).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            NameMapping nameMapping = mappingWrapper.getNameMappings().get(key.getName());
            if (nameMapping != null) {
                try (StringWriter writer = new StringWriter()) {
                    ve.mergeTemplate(nameMapping.getTemplate(), ENCODING, context, writer);
                    sb.append(writer).append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return sb.toString();
    }

    public VelocityContext handleBlockContext(BlockContext blockContext) {
        VelocityContext velocityContext = new VelocityContext();
        blockContext.getPropertyMap().forEach((propName, propVal) -> {
            StereotypeNamePair stereotypeNamePairFromQualifiedName = getStereotypeNamePairFromQualifiedName(propName);
            if (stereotypeNamePairFromQualifiedName != null) {
                StereotypeMapping stereotypeMapping = mappingWrapper.getStereotypeMappings().get(stereotypeNamePairFromQualifiedName.getStereoName());
                if (stereotypeMapping != null) {
                    String remappedName = stereotypeMapping.getProperties().get(stereotypeNamePairFromQualifiedName.getAttributeName());
                    velocityContext.put(remappedName, propVal);
                }
            } else {
                velocityContext.put(propName, propVal);
            }
        });
        blockContext.getLinkedPartContexts().forEach((propName, bcList) -> bcList.forEach(bc -> {
            VelocityContext context = handleBlockContext(bc);
            mergeContexts(velocityContext, context);
        }));
        return velocityContext;
    }

    public static String getNameFromQualifiedName(String qualifiedName) {
        if (qualifiedName != null) {
            if (!qualifiedName.isEmpty()) {
                return qualifiedName.substring(qualifiedName.lastIndexOf("::") + 2);
            } else {
                return "";
            }
        } else {
            return "null";
        }
    }

    public static StereotypeNamePair getStereotypeNamePairFromQualifiedName(String qualifiedName) {
        StereotypeNamePair pair = null;
        if (qualifiedName != null) {
            if (!qualifiedName.isEmpty()) {
                String[] split = qualifiedName.split("::");
                if (split.length > 1) {
                    pair = new StereotypeNamePair(split[split.length - 2], split[split.length - 1]);
                }
            }
        }
        return pair;
    }

    public static void mergeContexts(VelocityContext superContext, final VelocityContext contextToMerge) {
        for (String key : contextToMerge.getKeys()) {
            Object o = contextToMerge.get(key);
            if (!superContext.containsKey(key)) {
                superContext.put(key, o);
            }
        }
    }


}
