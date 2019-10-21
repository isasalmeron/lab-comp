/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import java.util.ArrayList;
import java.util.Arrays;

import lexer.Token;

public class Qualifier {
	
	private ArrayList<Token> qualifiers = new ArrayList<>();
	
	public Qualifier(final Token q) {
		qualifiers.add(q);
	}
	
	public Qualifier(final Token q1, final Token q2) {
		qualifiers.addAll(Arrays.asList(q1, q2));
	}
	
	public Qualifier(final Token q1, final Token q2, final Token q3) {
		qualifiers.addAll(Arrays.asList(q1, q2, q3));
	}
	
	public ArrayList<Token> getQualifiers() {
		return this.qualifiers;
	}
}
