/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class ClassDec {
	
	private String name;
	private String superclass;

	public ClassDec(final String name, String superclass) {
		this.name = name;
		this.superclass = superclass;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSuperclass() {
		return this.superclass;
	}
}
