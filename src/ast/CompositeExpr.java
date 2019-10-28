package ast;

import lexer.Token;

public class CompositeExpr extends Expr {
	Expr left, right;
	Token operator;
	
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
		
	}
}
