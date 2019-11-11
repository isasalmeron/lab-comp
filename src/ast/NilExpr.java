/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class NilExpr extends Expr {
	
	public NilExpr() {}

	@Override
	public void genJava(PW pw) {
		pw.print("null");
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.nilType;
	}
}
