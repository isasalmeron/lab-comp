/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;
import java.util.List;

public class ClassDec extends Type {
	
	private String name;
	private ClassDec superclass;
	private List<Member> memberList;

	public ClassDec(final String name, ClassDec superclass) {
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
	
	public MethodDec findMethod(ClassDec c, String methodName) {
		if (c == null) {
			return null;
		}
		
		for (Member m : c.memberList) {
			if (m instanceof MethodDec) {
				MethodDec castedMethod = (MethodDec) m;
				
				if (methodName.equals(castedMethod.getName())) {
					return castedMethod;
				}
			}
		}

		return null;
	}
	
	public Variable findVariable(ClassDec c, String variableName) {
		if (c == null) {
			return null;
		}
		
		for (Member m : c.memberList) {
			if (m instanceof Variable) {
				Variable casteVariable = (Variable) m;
				
				if (variableName.equals(casteVariable.getName())) {
					return casteVariable;
				}
			}
		}
		return null;
	}
	
	public Variable findVariableInSuper(String variableName) {
		if (superclass == null) {
			return null;
		}
		
		return this.findVariable(superclass, variableName);
	}
	
	public MethodDec findMethodInSuper(String methodName) {
		if (superclass == null) {
			return null;
		}
		
		return this.findMethod(superclass, methodName);
	}
	
	public Member findInSuper(String memberName) {
		Member result;
		
		if ((result =this.findVariable(superclass, memberName)) != null) {
			return result;
		}
		
		return this.findMethod(superclass, memberName);
	}
	
	public List<Member> getMemberList() {
		return this.memberList;
	}
	
	public ClassDec getSuperclass() {
		return this.superclass;
	}
	
	public void genJava(PW pw) {
		pw.print("public class ");
		pw.print(name);
		if (superclass != null) {
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
