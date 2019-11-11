package ast;

public class FieldExpr extends Expr {
	FieldDec field;
	
	@Override
	public Type getType() {
		return field.getType();
	}

	@Override
	public void genJava(PW pw) {
		field.genJava(pw);
	}
}
