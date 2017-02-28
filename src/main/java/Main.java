import java.util.*;


import org.apache.log4j.Logger;
import parser.MyJavaParser;


/**
 * Created by sonthai on 2/18/17.
 */
public class Main {
    final static Logger LOG = Logger.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("umlparser <source folder> <output file name>");
        } else {
            System.out.println(args[0] + "," + args[1]);
            //List<String> testcaseDirs = Arrays.asList("testcase1", "testcase2", "testcase3", "testcase4", "testcase5");
            //for (String s : testcaseDirs) {
                StringBuilder sb = new StringBuilder();
                //sb.append("testcases/").append(s);
                new MyJavaParser().parse(args[0], args[1]);
            //}
        }
    }
}
