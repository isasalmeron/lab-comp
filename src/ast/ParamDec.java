/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class ParamDec extends Member {
	
	private Type type;
	private String identifier;
	
	public ParamDec(final Type type, final String identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	public void genJava(PW pw) {
		pw.print(type.getName());
		pw.print(identifier);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
