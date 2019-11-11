package ast;

import java.util.List;

import lexer.Token;

public class MessageSendKeywordToFieldExpr extends Expr {
	
	ClassDec fieldReceiver;
	Token selector;
	List<Expr> argList;
	
	public MessageSendKeywordToFieldExpr(final ClassDec fieldReceiver, final Token selector, final List<Expr> argList) {
		this.fieldReceiver = fieldReceiver;
		this.selector = selector;
		this.argList = argList;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}

	@Override
	public Type getType() {
		return fieldReceiver;
	}
}
