package ast;

import java.util.List;

import lexer.Token;

public class MessageSendKeywordToFieldExpr extends Expr {
	FieldDec fieldReceiver;
	Token selector;
	List<Expr> argList;
	
	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub
		
	}
}
