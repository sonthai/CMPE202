package parser.rules;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.MemoMismatches;

import java.security.Key;

/**
 * Created by sonthai on 2/15/17.
 */
public class TypeModifierRuleDeclaration extends BaseParser<String> {
    UtilsRuleDeclaration utilsRule = Parboiled.createParser(UtilsRuleDeclaration.class);
    KeywordRule kw = Parboiled.createParser(KeywordRule.class);
    ConstantRule constantRule = Parboiled.createParser(ConstantRule.class);

    @MemoMismatches
    public Rule BasicType() {
        return Sequence(
                FirstOf("byte", "short", "char", "int", "long", "float", "double", "boolean"),
                ZeroOrMore(Sequence("[", "]"))
                /*TestNot(utilsRule.LetterOrDigit())*/
                //utilsRule.Spacing()
        );
    }

    public Rule Type() {
        return Sequence(FirstOf(BasicType(), ClassType()), ZeroOrMore(Sequence('[', ']')));
    }

    public Rule ReferenceType () {
        return FirstOf(
                Sequence(BasicType(), OneOrMore(Sequence('[', ']') )),
                Sequence(ClassType(), ZeroOrMore(Sequence('[', ']')))
        );
    }

    public Rule ClassType() {
        return Sequence(
                kw.Identifier(),
                Optional(Sequence("<", kw.Identifier(), ">")), //ZeroOrMore('.', TypeParameter()), Optional(TypeParameters())),
                ZeroOrMore(Sequence("[", "]"))
        );//, Optional(TypeParameters()), ZeroOrMore('.', TypeParameter()), Optional(TypeParameters()));
    }

    public Rule TypeParameters() {
        return Sequence(
                '<',
                TypeParameter(),
                ZeroOrMore(',', TypeParameter()),
                '>'
        );
    }

    public Rule TypeParameter() {
        return Sequence(kw.Identifier(), Optional(constantRule.EXTENDS));
    }


    public Rule Modifier() {
        return Sequence(
                FirstOf("public", "protected", "private", "static", "abstract", "final", "native",
                        "synchronized", "transient", "volatile", "strictfp"),
                TestNot(utilsRule.LetterOrDigit()),
                utilsRule.Spacing()
        );
    }







}
