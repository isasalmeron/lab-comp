/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

public class IfStat extends Statement {
	
	private Expr expr;
	private List<Statement> ifStatement;
	private List<Statement> elseStatement;
    
	public IfStat(Expr expr, List<Statement> ifStatement, List<Statement> elseStatement) {
		this.expr = expr;
		this.ifStatement = ifStatement;
		this.elseStatement = elseStatement;
	}
	
	@Override
	public void genJava(PW pw) {
		pw.print("if ");
		expr.genJava(pw);
		pw.print("{");
		pw.add();
		for (Statement statement : ifStatement) {
			statement.genJava(pw);
		}
		pw.sub();
		pw.print("}");
		
		if (elseStatement != null) {
			pw.print(" else {");
			pw.add();
			for (Statement statement : elseStatement) {
				statement.genJava(pw);
			}
			pw.sub();
			pw.print("}");
		}
	}
}
