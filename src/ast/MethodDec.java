/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

public class MethodDec extends Member {
	
	private String identifier;
	private List<ParamDec> formalParamDec;
	private List<Statement> stmtList;
	private Type returnType;
    
    public MethodDec(final String identifier, final List<ParamDec> formalParamDec,
    		List<Statement> stmtList, Type returnType) {
    	this.identifier = identifier;
    	this.formalParamDec = formalParamDec;
    	this.stmtList = stmtList;
    	this.returnType = returnType;
    }
    
    public MethodDec(final String identifier, List<Statement> stmtList, Type returnType) {
    	this.identifier = identifier;
    	this.stmtList = stmtList;
    	this.returnType = returnType;
    }

	@Override
	public void genJava(PW pw) {
		
	}
}
