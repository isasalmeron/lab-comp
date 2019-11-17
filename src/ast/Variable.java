package ast;

import lexer.Token;

public class Variable {
	
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
	
	public Type getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}

}
