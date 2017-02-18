package parser;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import parser.models.JavaClass;
import parser.models.Method;
import parser.rules.ClassRuleDeclaration;
import parser.utils.Util;

import java.util.List;

/**
 * Created by sonthai on 2/17/17.
 */
public class JavaParser {
    ClassRuleDeclaration classParser = Parboiled.createParser(ClassRuleDeclaration.class);
    static JavaParser jp = new JavaParser();
    public static void main(String [] args) throws Exception {
        Util util = new Util();
        List<String> files = util.getFilePaths("testcases");
        for (String f: files) {
            System.out.println(util.getFile(f));
            System.out.println("======================================");
            jp.run(util.getFile(f));
        }
    }

    public void run(String input) throws Exception {
        ParsingResult<?> result = new RecoveringParseRunner<JavaClass>(classParser.JavaClass()).run(input);
        JavaClass javaClass = (JavaClass) result.resultValue;

        System.out.println("Class Variable " + javaClass.classVariables().toString());
        System.out.println("Class Name " + javaClass.className());
        for (Method s : javaClass.methods().values()) {
            System.out.printf("Modifier %s type %s VarName %s\n", s.getModifier(), s.getType(), s.getVarName());
        }
    }
}
