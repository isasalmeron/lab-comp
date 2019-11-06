/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class ParamDec {
	
	private Type type;
	private String identifier;
	
	public ParamDec(final Type type, final String identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}
}
