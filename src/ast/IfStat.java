/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class IfStat extends Statement {
	
	private Expr expr;
	private Statement ifStatement;
	private Statement elseStatement;
    
	public IfStat(Expr expr, Statement ifStatement, Statement elseStatement) {
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
		ifStatement.genJava(pw);
		pw.sub();
		pw.print("}");
		
		if (elseStatement != null) {
			pw.print(" else {");
			pw.add();
			elseStatement.genJava(pw);
			pw.sub();
			pw.print("}");
		}
	}
	
	public Expr getExpr() {
		return expr;
	}

	public Statement getIfStatement() {
		return ifStatement;
	}
	
	public Statement getElseStatement() {
		return elseStatement;
	}
}
