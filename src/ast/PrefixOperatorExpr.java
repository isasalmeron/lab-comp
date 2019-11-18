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

	@Override
	public Type getType() {
		
		if (expr == null) {
			return Type.undefinedType;
		}
		
		if (operator == null) {
			return expr.getType();
		}
		
		if (operator == Token.NOT && expr.getType() == Type.booleanType) {
			return Type.booleanType;
		}
		
		if ((operator == Token.MINUS || operator == Token.PLUS) && expr.getType() == Type.intType) {
			return Type.intType;
		}
		
		return Type.undefinedType;
	}
}
