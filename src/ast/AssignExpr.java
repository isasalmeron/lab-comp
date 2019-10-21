/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class AssignExpr extends Expr {
	
	private Expr leftExpr;
	private Expr rightExpr;
    
	public AssignExpr(Expr leftExpr, Expr rightExpr) {
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
	}
	
	@Override
	public void genJava(PW pw) {
		leftExpr.genJava(pw);
		pw.print(" = ");
		rightExpr.genJava(pw);
		pw.print(";");
	}
	
	public Expr getLeftExpr() {
		return leftExpr;
	}
	
	public Expr getRightExpr() {
		return rightExpr;
	}
}
