package ast;

import java.util.List;

public class MessageSendKeywordExpr extends MessageSendExpr {
	String messageName;
	List<Expr> argList;
}
