/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class ReturnStat extends Statement {

	public ReturnStat(Expr expr) {
		this.expr = expr;
	}

	public Expr getExpr() {
		return expr;
	}

	private Expr expr;
}
