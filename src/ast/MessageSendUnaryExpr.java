/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class MessageSendUnaryExpr extends MessageSendExpr {
	
	Member message;
	
	public MessageSendUnaryExpr(final String receiver, final Member message) {
		super(receiver);
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
