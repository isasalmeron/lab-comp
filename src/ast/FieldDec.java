/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

import lexer.Token;

public class FieldDec extends Member {
	private Qualifier qualifier;
	private Type type;
	private List<Variable> variables;
	
	public FieldDec(final Qualifier qualifier, final Type type, final List<Variable> variables) {
		this.qualifier = qualifier;
		this.type = type;
		this.variables = variables;
	}
	
	public FieldDec(final Type type, final List<Variable> variables) {
		this.qualifier = new Qualifier(Token.PRIVATE);
		this.type = type;
		this.variables = variables;
	}
	
	public Qualifier getQualifier() {
		return this.qualifier;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public List<Variable> getVariables() {
		return this.variables;
	}

	@Override
	public void genJava(PW pw) {
		pw.print("private" + type.getName() + " ");
		
		int size = variables.size();
		for (int i = 0; i < size; i++) {
			pw.print(variables.get(i).getName());
			
			if (i != size - 1) {
				pw.print(",");
			}
		}
		pw.print(";");
	}
}
