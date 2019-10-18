/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;

public class RepeatStat extends Statement {
    
	public RepeatStat(ArrayList<Statement> statemtList, Expr expr) {
		this.statemtList = statemtList;
		this.expr = expr;
	}

	public ArrayList<Statement> getStatementList() {
		return statemtList;
	}

	public Expr getExpr() {
		return expr;
	}

	private ArrayList<Statement> statemtList;
	private Expr expr;
}
