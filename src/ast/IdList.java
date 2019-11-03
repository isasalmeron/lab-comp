package ast;

import java.util.List;

public class IdList {

	private List<String> identifiers;
	
	public IdList(final List<String> identifiers) {
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
