/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

public class WhileStat extends Statement {
	
	private Expr expr;
	private List<Statement> statementList;
    
	public WhileStat(Expr expr, List<Statement> statementList) {
		this.expr = expr;
		this.statementList = statementList;
	}
	
	@Override
	public void genJava(PW pw) {
		pw.print("while (");
		expr.genJava(pw);
		pw.print(") {");
		pw.add();
		
		int size = statementList.size();
		for (int i = 0; i < size; i++) {
			statementList.get(i).genJava(pw);
		}
		
		pw.sub();
		pw.print("}");
	}
}
