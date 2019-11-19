/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class MessageSendUnaryExpr extends MessageSendExpr {
	
	String receiver;
	Member message;
	
	public MessageSendUnaryExpr(final String receiver, final Member message) {
		super(receiver);
		this.receiver = receiver;
		this.message = message;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}

	@Override
	public Type getType() {
		return this.message.getType();
	}
}