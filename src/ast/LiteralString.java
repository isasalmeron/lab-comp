/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class LiteralString extends Expr {
	
	private String literalString;
    
    public LiteralString(String literalString) { 
        this.literalString = literalString;
    }
    
    @Override
    public void genJava(PW pw) {
        pw.print(literalString);
    }
}
