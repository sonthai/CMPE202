package parser.utils;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonthai on 2/17/17.
 */
public class Util {
    final static Logger LOG = Logger.getLogger(Util.class);

    public String getFile(String fileName) {
        String filePath =  "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            filePath = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            LOG.error("Failed to read file " + fileName);
        }

        return filePath;
    }

    public List<String> getFilePaths(String dirPath) {
        StringBuilder sb;
        List<String> filePaths =  new ArrayList<String>();

        List<String> files = getListFiles(dirPath);
        for (String f: files) {
            sb = new StringBuilder();
            filePaths.add(sb.append(dirPath).append("/").append(f).toString());
        }

        return filePaths;
    }


    private List<String> getListFiles(String dirPath) {
        List<String> files =  new ArrayList<String>();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            String filePathStr = IOUtils.toString(classLoader.getResource(dirPath));
            for (String p: filePathStr.split("\n")) {
                files.add(p);
            }
        } catch (IOException e) {
            LOG.error("Failed to find path " + dirPath);
        }

        return files;
    }
}
