/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;
import java.util.List;

import lexer.Token;

public class MethodDec extends Member {
	
	private Qualifier qualifier;
	private String name;
	private List<Variable> formalParamDec;
	private List<Statement> stmtList;
	private Type returnType;
    
    public MethodDec(final Qualifier qualifier, final String name, final List<Variable> formalParamDec, Type returnType) {
    	this.qualifier = qualifier;
    	this.name = name;
    	this.formalParamDec = formalParamDec;
    	this.stmtList = new ArrayList<>();
    	this.returnType = returnType;
    }
    
    public MethodDec(final String name, final List<Variable> formalParamDec, Type returnType) {
    	this.qualifier = new Qualifier(Token.PUBLIC);
    	this.name = name;
    	this.formalParamDec = formalParamDec;
    	this.stmtList = new ArrayList<>();
    	this.returnType = returnType;
    }
    
    public MethodDec(final Qualifier qualifier, final String name, Type returnType) {
    	this.qualifier = qualifier;
    	this.name = name;
    	this.stmtList = new ArrayList<>();
    	this.returnType = returnType;
    }
    
    public MethodDec(final String name, Type returnType) {
    	this.qualifier = new Qualifier(Token.PUBLIC);
    	this.name = name;
    	this.stmtList = new ArrayList<>();
    	this.returnType = returnType;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public Type getReturnType() {
    	return this.returnType;
    }
    
    public void setStmtList(List<Statement> stmtList) {
    	this.stmtList = stmtList;
    }
    
    public Boolean isPublic() {
    	if (qualifier == null) {
    		return true;
    	}
    	return this.qualifier.hasPublicQualifier();
    }

	@Override
	public void genJava(PW pw) {
		// TODO
	}

	@Override
	public Type getType() {
		return this.returnType;
	}
}
