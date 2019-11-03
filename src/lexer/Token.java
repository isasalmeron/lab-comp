/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package lexer;

public enum Token {

    AND("&&"),
    ANNOT("~annotation"),
    ASSERT("assert"),
    ASSIGN("="),
    BOOLEAN("Boolean"),
    BREAK("break"),
    CLASS("class"),
    COMMA(","),
    CONCAT("++"),
    DIV("/"),
    DOT("."),
    ELSE("else"),
    END("end"),
    EOF("~eof"),
    EQ("=="),
    EXTENDS("extends"),
    FALSE("false"),
    FINAL("final"),
    FUNC("func"),
    GE(">="),
    GT(">"),
    ID("~ident"),
    IDCOLON("~ident:"),
    IF("if"),
    IN("In"),
    INT("Int"),
    LE("<="),
    LEFTCURBRACKET("{"),
    LEFTPAR("("),
    LITERALINT("~number"),
    LITERALSTRING("~literalString"),
    LT("<"),
    MINUS("-"),
    MINUS_GT("->"),
    MULT("*"),
    NEQ("!="),
    NEW("new"),
    NIL("nil"),
    NOT("!"),
    OR("||"),
    OVERRIDE("override"),
    PLUS("+"),
    PLUSPLUS("++"),
    PRIVATE("private"),
    PUBLIC("public"),
    READINT("readInt"),
    READSTRING("readString"),
    REPEAT("repeat"),
    RETURN("return"),
    RIGHTCURBRACKET("}"),
    RIGHTPAR(")"),
    SELF("self"),
    SEMICOLON(";"),
    SHARED("shared"),
    STRING("String"),
    SUPER("super"),
    TRUE("true"),
    UNTIL("until"),
    VAR("var"),
    VOID("void"),
    WHILE("while");

	Token(String name) {
		this.name = name;
	}

	@Override public String toString() {
		return name;
	}
	private String name;
}
