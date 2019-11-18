package ast;

public abstract class MessageSendExpr extends Expr {
	
	String receiverIdent; // self, super, id

	public MessageSendExpr(final String receiverIdent) {
		this.receiverIdent = receiverIdent;
	}
	
	@Override
	public void genJava(PW pw) {
		pw.print(receiverIdent);
	}
}
