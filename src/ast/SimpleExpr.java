/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;

public class SimpleExpr extends Expr {
	
	private final SumSubExpr expr;
	private final ArrayList<SumSubExpr> exprList;
	
	public SimpleExpr(final SumSubExpr expr, final ArrayList<SumSubExpr> exprList) {
		this.expr = expr;
		this.exprList = exprList;
	}

	@Override
	public void genJava(PW pw) {
		expr.genJava(pw);
		
		int size = exprList.size();
		for (int i = 0; i < size; i++) {
			pw.print(" + ");
			exprList.get(i).genJava(pw);
		}
	}
}
