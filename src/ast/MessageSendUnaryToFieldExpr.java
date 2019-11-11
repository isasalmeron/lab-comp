package ast;

import lexer.Token;

public class MessageSendUnaryToFieldExpr extends Expr {
	
	ClassDec fieldReceiver;
	Token unaryMessage;
	
	public MessageSendUnaryToFieldExpr(final ClassDec fieldReceiver, final Token unaryMessage) {
		this.fieldReceiver = fieldReceiver;
		this.unaryMessage = unaryMessage;
	}

	@Override
	public Type getType() {
		return fieldReceiver;
	}

	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub	
	}
}
