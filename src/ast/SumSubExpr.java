/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;

public class SumSubExpr extends Expr {
	
	private final ArrayList<Term> terms;
	private final ArrayList<String> lowOperators;
	
	public SumSubExpr(final ArrayList<Term> terms, final ArrayList<String> lowOperators) {
		this.terms = terms;
		this.lowOperators = lowOperators;
	}

	@Override
	public void genJava(PW pw) {
		terms.get(0).genJava(pw);
		
		int size = terms.size();
		for (int i = 1; i < size; i++) {
			pw.print(lowOperators.get(i));
			terms.get(i).genJava(pw);
		}
	}
}
