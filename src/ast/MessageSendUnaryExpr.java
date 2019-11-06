/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class MessageSendUnaryExpr extends MessageSendExpr {
	
	String messageName;
	
	public MessageSendUnaryExpr(final String messageName) {
		this.messageName = messageName;
	}
}
