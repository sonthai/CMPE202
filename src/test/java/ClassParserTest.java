import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import parser.models.JavaClass;
import parser.models.Method;
import parser.rules.ClassRuleDeclaration;
import parser.utils.Util;

import java.io.IOException;
import java.util.Map;

/**
 * Created by sonthai on 2/17/17.
 */
public class ClassParserTest {
    ClassRuleDeclaration classParser = Parboiled.createParser(ClassRuleDeclaration.class);

    @Test
    public void testClassParser() throws Exception {
        String input = getInput();
        ParsingResult<?> result = new RecoveringParseRunner<JavaClass>(classParser.ClassDeclaration()).run(input);
        JavaClass javaClass = (JavaClass) result.resultValue;

        System.out.println("Class Variable " + javaClass.classVariables().toString());
        System.out.println("Class Name " + javaClass.className());
        for (Method s : javaClass.methods().values()) {
            System.out.printf("Modifier %s type %s VarName %s\n", s.getModifier(), s.getType(), s.getVarName());
        }
    }

    public String getInput() {
        String input = getFile("testcases/A.java");
        return input;
    }

    private String getFile(String fileName) {
        String filePath =  "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            filePath = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
