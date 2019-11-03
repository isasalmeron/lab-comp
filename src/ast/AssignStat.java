package ast;

public class AssignStat extends Statement {
	private Expr expr;
	
	public AssignStat(final Expr expr) {
		this.expr = expr;
	}

	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub

	}

}
