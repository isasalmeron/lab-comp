/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

import lexer.Token;

public class PrintStat extends Statement {
	
	private Token printType;
	private List<Expr> exprList;

	public PrintStat(final Token printType, final List<Expr> exprList) {
		this.printType = printType;
		this.exprList = exprList;
	}
	
	@Override
	public void genJava(PW pw) {
		if (printType == Token.ID) {
			pw.print("System.out.print(");
		} else {
			pw.print("System.out.println(");
		}
		
		for (Expr expr : exprList) {
			expr.genJava(pw);
		}
		
		pw.print(");");
	}
}
