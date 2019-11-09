package ast;

public class FieldExpr extends Expr {
	FieldDec field;

	@Override
	public void genJava(PW pw) {
		field.genJava(pw);
	}
}
