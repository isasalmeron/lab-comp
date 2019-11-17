/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

public class ClassDec extends Type {
	
	private String name;
	private String superclass;
	private List<Member> memberList;

	public ClassDec(final String name, String superclass) {
		super(name);
		this.name = name;
		this.superclass = superclass;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
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
