/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import lexer.Token;

public class ReadExpr extends Expr {
	
	Token readType;
    
	public ReadExpr(final Token readType) {
		this.readType = readType;
	}
	
	public void genJava(PW pw) {
		// TODO
	}
}
