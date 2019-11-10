package ast;

import lexer.Token;

public class MessageSendExpr extends Expr {
	
	Token receiverIdent;
	
	@Override
	public void genJava(PW pw) {
		pw.print(receiverIdent.toString());
	}
}
