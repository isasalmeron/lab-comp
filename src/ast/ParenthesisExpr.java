/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class ParenthesisExpr extends Expr {
	
	Expr expr;

	public ParenthesisExpr(final Expr expr) {
		this.expr = expr;
	}
	
	@Override
	public Type getType() {
		return expr.getType();
	}
	
	@Override
	public void genJava(PW pw) {
		pw.print("(");
		expr.genJava(pw);
		pw.print(")");
	}
}
