/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class PrintStat extends Statement {

	public PrintStat(Expr expr) {
		this.expr = expr;
	}

	public Expr getExpr() {
		return expr;
	}

	private Expr expr;
}
