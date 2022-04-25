package at.vres.master.mdml.tbcg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DocOnceHandler {
    private static final String START_PYTHON_BLOCK = "!bc pycod\n";
    private static final String END_PYTHON_BLOCK = "\n!ec\n";
    private static final String TEST_DOC_PATH = "C:\\Users\\rup\\Downloads\\hplgit-doconce-1.4.13-17-g5e3e215\\hplgit-doconce-5e3e215\\bin\\doconce";

    public static void callDocOnceFromPython(String dotFileName) {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "doconce", "format", "ipynb", dotFileName);
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void callDocOnce(String dotFileName) {
        Runtime rt = Runtime.getRuntime();
        try {
            String com = "python " + TEST_DOC_PATH + " format ipynb " + dotFileName;
            System.out.println("com = " + com);
            Process pr = rt.exec("python doconce format ipynb " + dotFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String wrapPythonCode(String pyCode) {
        return START_PYTHON_BLOCK + pyCode + END_PYTHON_BLOCK;
    }

    public static void writeDotFile(String pyCode, String dotFilePath) {
        String py = wrapPythonCode(pyCode);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(dotFilePath))) {
            writer.write(py);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void doDotOnceGeneration(String pyCode, String dotFilePath) {
        writeDotFile(pyCode, dotFilePath);
        callDocOnce(dotFilePath.replace(".do.txt", ""));
    }
}
