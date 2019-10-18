/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

//tem que adc o idList
public class LocalDec extends Statement {
    
    public LocalDec(Type type, Expr expr) { 
    	this.type = type;
    	this.expr = expr;
    }
    
	public Type getType() {
		return type;
	}

	public Expr getExpr() {
		return expr;
	}

	private Type type;
	private Expr expr;
}
