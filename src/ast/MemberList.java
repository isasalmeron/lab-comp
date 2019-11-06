/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */


package ast;

public class MemberList {
	
	private Qualifier qualifier;
	private Member member;
	
	public MemberList(final Qualifier qualifier, Member member) {
		this.qualifier = qualifier;
		this.member = member;
	}
	
    @Override
	public void genJava(PW pw) {
		// TODO		
	}
}
