package uml.classdiagram;

import org.apache.log4j.Logger;
import parser.MyJavaParser;


/**
 * Created by sonthai on 2/18/17.
 */
public class Main {
    final static Logger LOG = Logger.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("umlparser <source folder> <output folder>");
        } else {
            System.out.println(args[0] + "," + args[1]);
            MyJavaParser.parse(args[0], args[1]);
        }
    }
}
