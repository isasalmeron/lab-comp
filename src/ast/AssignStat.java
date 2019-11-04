/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class AssignStat extends Statement {
	private Expr expr;
	
	public AssignStat(final Expr expr) {
		this.expr = expr;
	}

	@Override
	public void genJava(PW pw) {
		expr.genJava(pw);
	}
}
