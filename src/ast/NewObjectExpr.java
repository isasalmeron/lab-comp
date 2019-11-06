/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class NewObjectExpr extends Expr {
	
	String aClass;
	
	public NewObjectExpr(final String aClass) {
		this.aClass = aClass;
	}

	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}
}
