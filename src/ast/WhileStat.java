/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;

public class WhileStat extends Statement {
    
	public WhileStat(Expr expr, ArrayList<Statement> statemtList) {
		this.expr = expr;
		this.statemtList = statemtList;
	}
	
	public Expr getExpr() {
		return expr;
	}

	public ArrayList<Statement> getStatementList() {
		return statemtList;
	}
	
	private Expr expr;
	private ArrayList<Statement> statemtList;
}
