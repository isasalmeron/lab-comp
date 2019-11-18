package ast;

public class MessageSendUnaryToFieldExpr extends Expr {
	
	String receiverIdent;
	String middleFieldName;
	Member fieldReceiver;
	
	public MessageSendUnaryToFieldExpr(final String receiverIdent, final String middleFieldName, final Member fieldReceiver) {
		this.receiverIdent = receiverIdent;
		this.middleFieldName = middleFieldName;
		this.fieldReceiver = fieldReceiver;
	}

	@Override
	public Type getType() {
		return this.fieldReceiver.getType();
	}

	@Override
	public void genJava(PW pw) {
		// TODO Auto-generated method stub	
	}
}
