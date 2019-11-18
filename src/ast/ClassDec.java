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
		
		MethodDec result = null;
		
		for (Member m : c.memberList) {
			if (m instanceof MethodDec) {
				MethodDec castedMethod = (MethodDec) m;
				
				if (methodName.equals(castedMethod.getName())) {
					result = castedMethod;
					break;
				}
			}
		}
		
		if (result == null) {
			result = findMethod(c.superclass, methodName);
		}

		return result;
	}
	
	public Variable findVariable(ClassDec c, String variableName) {
		if (c == null) {
			return null;
		}
		
		Variable result = null;
		
		for (Member m : c.memberList) {
			if (m instanceof Variable) {
				Variable castedVariable = (Variable) m;
				
				if (variableName.equals(castedVariable.getName())) {
					result = castedVariable;
					break;
				}
			}
		}
		
		if (result == null) {
			result = findVariable(c.superclass, variableName);
		}
		
		return result;
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
	
	public Member findMember(ClassDec c, String memberName) {
		Member result;
		
		if ((result = this.findVariable(c, memberName)) != null) {
			return result;
		}
		
		return this.findMethod(c, memberName);
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
