/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import lexer.Token;

public class PrefixOperatorExpr extends Expr {
	Token operator;
	Expr expr;
	
	public PrefixOperatorExpr(final Token operator, final Expr expr) {
		this.operator = operator;
		this.expr = expr;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
		
	}
}
