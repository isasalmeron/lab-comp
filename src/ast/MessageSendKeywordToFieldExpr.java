package ast;

import java.util.List;

import lexer.Token;

public class MessageSendKeywordToFieldExpr extends Expr {
	
	String receiverIdent;
	String middleFieldName;
	Member fieldReceiver;
	List<Expr> argList;
	
	public MessageSendKeywordToFieldExpr(final String receiverIdent, final String middleFieldName,
			final Member fieldReceiver, final List<Expr> argList) {
		this.receiverIdent = receiverIdent;
		this.middleFieldName = middleFieldName;
		this.fieldReceiver = fieldReceiver;
		this.argList = argList;
	}
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
	}

	@Override
	public Type getType() {
		return this.fieldReceiver.getType();
	}
}
