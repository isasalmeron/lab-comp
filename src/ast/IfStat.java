/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;

public class IfStat extends Statement {
    
	public IfStat(Expr expr, ArrayList<Statement> ifStatemtList, ArrayList<Statement> elseStatemtList) {
		this.expr = expr;
		this.ifStatemtList = ifStatemtList;
		this.elseStatemtList = elseStatemtList;
	}
	
	public Expr getExpr() {
		return expr;
	}

	public ArrayList<Statement> getIfStatemtList() {
		return ifStatemtList;
	}
	
	public ArrayList<Statement> getElseStatemtList() {
		return elseStatemtList;
	}
	
	private Expr expr;
	private ArrayList<Statement> ifStatemtList;
	private ArrayList<Statement> elseStatemtList;
}
