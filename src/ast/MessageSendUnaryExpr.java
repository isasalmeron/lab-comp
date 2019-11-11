/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

import lexer.Token;

public class MessageSendUnaryExpr extends MessageSendExpr {
	
	String messageName;
	
	public MessageSendUnaryExpr(final Token receiver, final String messageName) {
		super(receiver);
		this.messageName = messageName;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
