package ast;

import java.util.ArrayList;

public class IdList {

	private ArrayList<String> identifiers;
	
	public IdList(final ArrayList<String> identifiers) {
		this.identifiers = identifiers;
	}
	
	public void genJava(PW pw) {
		int size = identifiers.size();
		for (int i = 0; i < size; i++) {
			pw.print(identifiers.get(i));
			
			if (i != size - 1) {
				pw.print(",");
			}
		}
		pw.print(";");
	}
}
