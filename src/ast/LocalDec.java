/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class LocalDec extends Statement {
	
	private Type type;
	private IdList idList;
	private Expr expr;
    
    public LocalDec(Type type, IdList idList, Expr expr) { 
    	this.type = type;
    	this.idList = idList;
    	this.expr = expr;
    }
    
    @Override
	public void genJava(PW pw) {
		type.getJavaName();
		idList.genJava(pw);
		pw.print(" = ");
		expr.genJava(pw);
	}
}
