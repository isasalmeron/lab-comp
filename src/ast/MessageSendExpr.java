package ast;

import lexer.Token;

public abstract class MessageSendExpr extends Expr {
	
	Token receiverIdent; // self, super

	public MessageSendExpr(final Token receiverIdent) {
		this.receiverIdent = receiverIdent;
	}
	
	@Override
	public void genJava(PW pw) {
		pw.print(receiverIdent.toString());
	}
}
