package parser.rules;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.Var;
import parser.models.JavaClass;
import parser.models.Method;

import java.util.Stack;

/**
 * Created by sonthai on 2/17/17.
 */
@BuildParseTree
public class ClassRuleDeclaration extends BaseParser<JavaClass> {
    KeywordRule kw = Parboiled.createParser(KeywordRule.class);
    TypeModifierRuleDeclaration tm = Parboiled.createParser(TypeModifierRuleDeclaration.class);
    UtilsRuleDeclaration utilsParser =  Parboiled.createParser(UtilsRuleDeclaration.class);
    JavaClass jc = new JavaClass();
    Method m = new Method();

    public Rule JavaClass() {
        return Sequence(ClassDeclaration(), EOI, push(jc));

    }
    public Rule ClassDeclaration() {
        return Sequence(
                Optional(Sequence(String("public"), ZeroOrMore(Ch(' ')))),
                String("class"), ZeroOrMore(Ch(' ')),
                kw.Identifier(),
                push(jc.className(match())),
                ClassBody()
        );
    }

    public Rule ClassBody() {
        return Sequence(Ch('{'),  ZeroOrMore(ClassBodyDeclaration()), Ch('}'));
    }

    public Rule ClassBodyDeclaration() {
        return ClassVariable();
    }

    public Rule ClassVariable() {
        Stack<String> tmp = new Stack<String>();
        return Sequence(
                NewLine(),
                FirstOf(
                        Sequence(
                                ZeroOrMore(Modifier()),
                                tmp.add(match()),
                                MemberDecl(),
                                push(jc.classVariable(tmp.pop() + match())),
                                Ch(';')
                        ),
                        ClassDeclaration()
                )

        );
    }

    Rule Modifier() {
        return Sequence(
                FirstOf("public", "protected", "private", "static", "abstract", "final", "native",
                        "synchronized", "transient", "volatile", "strictfp"),
                TestNot(utilsParser.LetterOrDigit()),
                Space()
        );
    }

    Rule Space() {
        return Ch(' ');
    }

    Rule NewLine() {
        return ZeroOrMore(AnyOf("\n "));
    }

    Rule Type() {
        return FirstOf(tm.BasicType(), tm.ClassType());
    }


    public Rule MemberDecl() {
        return Sequence(
                Type(), Space(), utilsParser.LetterOrDigit());
    }

    public Rule MethodBody() {
        return null;
    }
}
