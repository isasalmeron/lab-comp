/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class FieldDec extends Member {
	private Type type;
	private IdList identifiers;
	
	public FieldDec(final Type type, final IdList identifiers) {
		this.type = type;
		this.identifiers = identifiers;
	}
	
	public Type getType() {
		return this.type;
	}

	@Override
	public void genJava(PW pw) {
		pw.print(type.getName() + " ");
		identifiers.genJava(pw);
	}
}
