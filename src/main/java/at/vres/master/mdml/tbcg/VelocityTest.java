package at.vres.master.mdml.tbcg;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

public class VelocityTest {

    public static void velTestRun(String templateName) {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "C:\\Users\\rup\\IdeaProjects\\MasterModelDrivenML\\templates");
        VelocityEngine ve = new VelocityEngine();
        ve.init(p);

        //Template t = ve.getTemplate(templateName)
        // import pandas as pd
        //
        //$var_name = pd.read_csv($path, sep=$delimiter, encoding=$encoding, skiprows=$nr_skip_lines
        VelocityContext context = new VelocityContext();
        context.put("var_name", "df_one");
        context.put("path", "here/test.csv");
        context.put("delimiter", ",");
        context.put("encoding", "UTF-8");
        context.put("nr_skip_lines", 0);

        try (StringWriter sw = new StringWriter()) {
            ve.mergeTemplate(templateName, "UTF-8", context, sw);
            System.out.println("TEST: \n" + sw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
