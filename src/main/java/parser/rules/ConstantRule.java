package parser.rules;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;

/**
 * Created by sonthai on 2/15/17.
 */
public class ConstantRule extends BaseParser<String> {
    KeywordRule kw = Parboiled.createParser(KeywordRule.class);

    public final Rule ASSERT = kw.Keyword("assert");
    public final Rule BREAK = kw.Keyword("break");
    public final Rule CASE = kw.Keyword("case");
    public final Rule CATCH = kw.Keyword("catch");
    public final Rule CLASS = kw.Keyword("class");
    public final Rule CONST = kw.Keyword("const");
    public final Rule CONTINUE = kw.Keyword("continue");
    public final Rule DEFAULT = kw.Keyword("default");
    public final Rule DO = kw.Keyword("do");
    public final Rule ELSE = kw.Keyword("else");
    public final Rule ENUM = kw.Keyword("enum");
    public final Rule EXTENDS = kw.Keyword("extends");
    public final Rule FINALLY = kw.Keyword("finally");
    public final Rule FINAL = kw.Keyword("final");
    public final Rule FOR = kw.Keyword("for");
    public final Rule GOTO = kw.Keyword("goto");
    public final Rule IF = kw.Keyword("if");
    public final Rule IMPLEMENTS = kw.Keyword("implements");
    public final Rule IMPORT = kw.Keyword("import");
    public final Rule INTERFACE = kw.Keyword("interface");
    public final Rule INSTANCEOF = kw.Keyword("instanceof");
    public final Rule NEW = kw.Keyword("new");
    public final Rule PACKAGE = kw.Keyword("package");
    public final Rule RETURN = kw.Keyword("return");
    public final Rule STATIC = kw.Keyword("static");
    public final Rule SUPER = kw.Keyword("super");
    public final Rule SWITCH = kw.Keyword("switch");
    public final Rule SYNCHRONIZED = kw.Keyword("synchronized");
    public final Rule THIS = kw.Keyword("this");
    public final Rule THROWS = kw.Keyword("throws");
    public final Rule THROW = kw.Keyword("throw");
    public final Rule TRY = kw.Keyword("try");
    public final Rule VOID = kw.Keyword("void");
    public final Rule WHILE = kw.Keyword("while");
}
