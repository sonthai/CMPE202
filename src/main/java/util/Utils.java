package util;

import model.ClassPojo;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonthai on 2/20/17.
 */
public class Utils {
    final static Logger LOG = Logger.getLogger(Utils.class);

    public List<String> getFilePaths(String sourceDir) {
        StringBuilder sb;
        List<String> filePaths =  new ArrayList<String>();

        List<String> files = getListFiles(sourceDir);
        for (String f: files) {
            sb = new StringBuilder();
            filePaths.add(sb.append(sourceDir).append("/").append(f).toString());
        }

        return filePaths;
    }


    private List<String> getListFiles(String sourceDir) {
        List<String> files =  new ArrayList<String>();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            String dirStr = IOUtils.toString(classLoader.getResource(sourceDir));

            for (String p: dirStr.split("\n")) {
                if (p.endsWith(".java")) {
                    files.add(p);
                }
            }
        } catch (IOException e) {
            LOG.error("Failed to find path " + sourceDir);
        }

        return files;
    }

    public static String prettyJson(List<ClassPojo> pjs) {
        String jsonStr = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            jsonStr = mapper.writeValueAsString(pjs);
            System.out.println(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }
}