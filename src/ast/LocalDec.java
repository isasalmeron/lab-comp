/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

public class LocalDec extends Statement {
	
	private Type type;
	private List<Variable> variables;
	private Expr expr;
    
    public LocalDec(Type type, List<Variable> variables, Expr expr) { 
    	this.type = type;
    	this.variables = variables;
    	this.expr = expr;
    }
    
    @Override
	public void genJava(PW pw) {
		type.getName();
		
		int size = variables.size();
		for (int i = 0; i < size; i++) {
			pw.print(variables.get(i).getName());
			
			if (i != size - 1) {
				pw.print(",");
			}
		}
		
		pw.print(" = ");
		expr.genJava(pw);
	}
}
