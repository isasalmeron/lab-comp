/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class NewObjectExpr extends Expr {
	
	ClassDec aClass;
	
	public NewObjectExpr(final ClassDec aClass) {
		this.aClass = aClass;
	}

	@Override
	public void genJava(PW pw) {
		pw.print("new ");
		pw.print(aClass.getName());
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
