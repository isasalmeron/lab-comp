/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

abstract public class Expr {
	
    abstract public void genJava(PW pw);
    
    abstract public Type getType();
}
