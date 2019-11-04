/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class AssertStat extends Statement {
	
	private Expr expr;
	private String value;
	
	public AssertStat(final Expr expr, final String value) {
		this.expr = expr;
		this.value = value;
	}

	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}
}
