/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class AssignExpr extends Statement {
    
	public AssignExpr(Expr leftExpr, Expr rightExpr) {
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
	}
	
	public Expr getLeftExpr() {
		return leftExpr;
	}
	
	public Expr getRightExpr() {
		return rightExpr;
	}
	
	private Expr leftExpr;
	private Expr rightExpr;
}
