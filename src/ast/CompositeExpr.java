/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import lexer.Token;

public class CompositeExpr extends Expr {
	
	Expr left, right;
	Token operator;
	
	public CompositeExpr(final Expr left, final Token operator, final Expr right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}
}
