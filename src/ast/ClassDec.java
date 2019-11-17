/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;
import java.util.List;

public class ClassDec extends Type {
	
	private String name;
	private String superclass;
	private List<Member> memberList;

	public ClassDec(final String name, String superclass) {
		super(name);
		this.name = name;
		this.superclass = superclass;
		this.memberList = new ArrayList<>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}
	
	public MethodDec findMethod(String methodName) {
		for (Member m : this.memberList) {
			if (m instanceof MethodDec) {
				MethodDec castedMethod = (MethodDec) m;
				
				if (methodName.equals(castedMethod.getName())) {
					return castedMethod;
				}
			}
		}

		return null;
	}
	
	public Variable findVariable(String variableName) {
		for (Member m : this.memberList) {
			if (m instanceof Variable) {
				Variable casteVariable = (Variable) m;
				
				if (variableName.equals(casteVariable.getName())) {
					return casteVariable;
				}
			}
		}
		return null;
	}
	
	public List<Member> getMemberList() {
		return this.memberList;
	}
	
	public void genJava(PW pw) {
		pw.print("public class ");
		pw.print(name);
		if (superclass != "") {
			pw.print(" extends " + superclass);
		}
		pw.print(" {");
		pw.add();
		for (Member member : memberList) {
			member.genJava(pw);
		}
		pw.sub();
		pw.println("}");
	}
}
