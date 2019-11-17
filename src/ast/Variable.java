package ast;

import lexer.Token;

public class Variable extends Member {
	
	private Qualifier qualifier;
	private Type type;
	private String name;
	
	public Variable(final Qualifier qualifier, final Type type, final String name) {
		this.qualifier = qualifier;
		this.type = type;
		this.name = name;
	}
	
	public Variable(final Type type, final String name) {
		this.qualifier = new Qualifier(Token.PUBLIC);
		this.type = type;
		this.name = name;
	}
	
	public Qualifier getQualifier() {
		return this.qualifier;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
		
	}

}
