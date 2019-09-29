/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package comp;

public class CompilerError extends RuntimeException {
	public CompilerError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() { return errorMessage; }

	private String errorMessage;
	private static final long serialVersionUID = 1L;

}
