package ast;

import java.util.List;

public class MessageSendKeywordExpr extends MessageSendExpr {
	String messageName;
	List<Expr> argList;
	
	public MessageSendKeywordExpr(final String messageName, final List<Expr> argList) {
		this.messageName = messageName;
		this.argList = argList;
	}
}
