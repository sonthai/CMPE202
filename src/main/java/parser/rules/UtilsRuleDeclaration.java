package parser.rules;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;

/**
 * Created by sonthai on 2/15/17.
 */
public class UtilsRuleDeclaration extends BaseParser<String> {
    public Rule Letter() {
        return FirstOf(
                CharRange('a', 'z'),
                CharRange('A', 'Z'),
                '_',
                '$'
        );
    }

    public Rule Digit() {
        return CharRange('0', '9');
    }

    @MemoMismatches
    public Rule LetterOrDigit() {
        return FirstOf(
                CharRange('a', 'z'),
                CharRange('A', 'Z'),
                CharRange('0', '9'),
                '_',
                '$'
        );
    }
    @SuppressNode
    @DontLabel
    public Rule Terminal(String string) {
        return Sequence(string, Spacing()).label('\'' + string + '\'');
    }

    @SuppressNode
    @DontLabel
    public Rule Terminal(String string, Rule notFollow) {
        return Sequence(string, TestNot(notFollow), Spacing()).label('\'' + string + '\'');
    }

    @SuppressNode
    public Rule Spacing() {
        return ZeroOrMore (FirstOf(WhiteSpace(), Comment(), EndOfLineComment()));

    }

    public Rule WhiteSpace() {
        return OneOrMore(AnyOf(" \t\r\n\f")).label("Whitespace");
    }

    public Rule Comment() {
        return Sequence(
                "/*",
                ZeroOrMore(TestNot("*/"), ANY),
                "*/"
        );
    }

    public Rule EndOfLineComment() {
        return Sequence(
                "//",
                ZeroOrMore(TestNot(AnyOf("\r,\n")), ANY),
                FirstOf("\r\n", '\r', '\n', EOI)
        );
    }
}
