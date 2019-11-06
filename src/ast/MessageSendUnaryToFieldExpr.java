package ast;

import lexer.Token;

public class MessageSendUnaryToFieldExpr extends Expr {
	
	FieldDec fieldReceiver;
	Token unaryMessage;

	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub	
	}
}
