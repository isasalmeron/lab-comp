/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class TypeNull extends Type {

	public TypeNull() {
		super("nil");	//nao sei se fiz certo
	}

	@Override
	public String getCname() {
		return "NULL";
	}

}
