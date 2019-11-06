/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class LiteralInt extends Expr {
	
	private int value;
    
    public LiteralInt(int value) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public void genJava(PW pw) {
        pw.printIdent("" + value);
    }
}
