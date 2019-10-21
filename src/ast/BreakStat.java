package ast;

public class BreakStat extends Statement {
	
	public BreakStat() {}

	@Override
	public void genJava(PW pw) {
		pw.print("break;");
	}
}
