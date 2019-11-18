package ast;

import java.util.List;

import lexer.Token;

public class MessageSendKeywordExpr extends MessageSendExpr {
	
	Member member;
	List<Expr> argList;
	
	public MessageSendKeywordExpr(final String receiver, final Member member, final List<Expr> argList) {
		super(receiver);
		this.member = member;
		this.argList = argList;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}

	@Override
	public Type getType() {
		return this.member.getType();
	}
}
