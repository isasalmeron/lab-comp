package ast;

import java.util.List;

import lexer.Token;

public class MessageSendKeywordToFieldExpr extends Expr {
	
	FieldDec fieldReceiver;
	Token selector;
	List<Expr> argList;
	
	public MessageSendKeywordToFieldExpr(final FieldDec fieldReceiver, final Token selector, final List<Expr> argList) {
		this.fieldReceiver = fieldReceiver;
		this.selector = selector;
		this.argList = argList;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}
}
