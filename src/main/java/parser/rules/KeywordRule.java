package parser.rules;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;

/**
 * Created by sonthai on 2/15/17.
 */
public class KeywordRule extends BaseParser<String> {
    UtilsRuleDeclaration utilRule = Parboiled.createParser(UtilsRuleDeclaration.class);
    @SuppressSubnodes
    @MemoMismatches
    public Rule Identifier() {
        return Sequence(TestNot(Keyword()), utilRule.Letter(), ZeroOrMore(utilRule.LetterOrDigit()));//utilRule.Spacing());
    }

    @MemoMismatches
    public Rule Keyword() {
        return Sequence(
                FirstOf("assert", "break", "case", "catch", "class", "const", "continue", "default", "do", "else",
                        "enum", "extends", "finally", "final", "for", "goto", "if", "implements", "import", "interface", "instanceof", "new",
                        "package", "return", "static", "super", "switch", "synchronized", "this", "throws", "throw", "try", "void", "while"),
                TestNot(utilRule.LetterOrDigit())

        );
    }

    @SuppressNode
    @DontLabel
    public Rule Keyword(String keyword) {
        return utilRule.Terminal(keyword, utilRule.LetterOrDigit());
    }

}
