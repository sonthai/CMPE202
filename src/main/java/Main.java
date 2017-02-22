import java.util.*;


import org.apache.log4j.Logger;
import parser.MyJavaParser;


/**
 * Created by sonthai on 2/18/17.
 */
public class Main {
    final static Logger LOG = Logger.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        List<String> testcaseDirs = Arrays.asList("testcase1", "testcase2");
        for (String s: testcaseDirs) {
            StringBuilder sb = new StringBuilder();
            sb.append("testcases/").append(s);
            new MyJavaParser().parse(sb.toString());
        }
    }
}
