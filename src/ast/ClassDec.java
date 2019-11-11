/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.List;

public class ClassDec extends Type {
	
	private String name;
	private String superclass;
	private List<MemberList> memberList;

	public ClassDec(final String name, String superclass, List<MemberList> memberList) {
		super(name);
		this.name = name;
		this.superclass = superclass;
		this.memberList = memberList;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void genJava(PW pw) {
		pw.print("public class ");
		pw.print(name);
		if (superclass != "") {
			pw.print(" extends " + superclass);
		}
		pw.print(" {");
		pw.add();
		for (MemberList list : memberList) {
			list.genJava(pw);
		}
		pw.sub();
		pw.println("}");
	}
}
