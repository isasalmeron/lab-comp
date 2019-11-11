package ast;

import java.util.List;

import lexer.Token;

public class MessageSendKeywordExpr extends MessageSendExpr {
	
	String methodName;
	List<Expr> argList;
	
	public MessageSendKeywordExpr(final Token receiver, final String methodName, final List<Expr> argList) {
		super(receiver);
		this.methodName = methodName;
		this.argList = argList;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
