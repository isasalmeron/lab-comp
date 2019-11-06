/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

public class RepeatStat extends Statement {
	
	private List<Statement> statementList;
	private Expr expr;
    
	public RepeatStat(List<Statement> statementList, Expr expr) {
		this.statementList = statementList;
		this.expr = expr;
	}
	
	@Override
	public void genJava(PW pw) {
		pw.print("do {");
		pw.add();
		
		int size = statementList.size();
		for (int i = 0; i < size; i++) {
			statementList.get(i).genJava(pw);
		}
		
		pw.sub();
		pw.print("} while (");
		
		expr.genJava(pw);
		pw.print(");");
	}
}
