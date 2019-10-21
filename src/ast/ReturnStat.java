/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class ReturnStat extends Statement {
	
	private Expr expr;

	public ReturnStat(Expr expr) {
		this.expr = expr;
	}
	
	@Override
	public void genJava(PW pw) {
		pw.print("return");
		expr.genJava(pw);
		pw.print(";");
	}

	public Expr getExpr() {
		return this.expr;
	}
}
