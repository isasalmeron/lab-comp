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
	public Type getType() {
		Type leftType = left.getType();
		Type rightType = right.getType();

		if (operator == Token.EQ || operator == Token.NEQ) {
			if (leftType == rightType) {
				return Type.booleanType;
			}

		} else if (operator == Token.LT 
				|| operator == Token.GT
				|| operator == Token.LE
				|| operator == Token.GE) {
			if (leftType == Type.intType && rightType == Type.intType) {
				return Type.booleanType;
			}
		}
		
		if (operator == Token.PLUS || operator == Token.MINUS || operator == Token.MULT || operator == Token.DIV) {
			if (leftType == Type.intType && rightType == Type.intType) {
				return Type.intType;
			}
		}
		
		if (operator == Token.OR || operator == Token.AND) {
			if (leftType == Type.booleanType && leftType == rightType) {
				return Type.booleanType;
			}
		}
		
		if (operator == Token.PLUSPLUS) {
			if ((leftType == Type.intType || leftType == Type.stringType)
					&& (rightType == Type.intType || rightType == Type.stringType)) {
				return Type.stringType;
			}
		}

		return Type.undefinedType;
	}
	
	@Override
	public void genJava(PW pw) {
		left.genJava(pw);
		pw.print(" " + operator + " ");
		right.genJava(pw);
	}
}
