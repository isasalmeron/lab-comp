/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class LiteralBoolean extends Expr {
	
	public static LiteralBoolean True  = new LiteralBoolean(true);
    public static LiteralBoolean False = new LiteralBoolean(false);

    private boolean value;

    public LiteralBoolean( boolean value ) {
        this.value = value;
    }

    @Override
	public void genJava(PW pw) {
       pw.print( value ? "true" : "false" );
    }
}
