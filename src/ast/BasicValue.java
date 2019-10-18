/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class BasicValue extends Factor {
	
	private Integer intValue;
	private Boolean booleanValue;
	private String stringValue;
	private Type type;

	public BasicValue(Integer intValue) {
		this.intValue = intValue;
		this.type = Type.intType;
	}
	
	public BasicValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
		this.type = Type.booleanType;
	}
	
	public BasicValue(String stringValue) {
		this.stringValue = stringValue;
		this.type = Type.stringType;
	}
	
	public Integer getIntValue() {
		return this.intValue;
	}
	
	public Boolean getBooleanValue() {
		return this.booleanValue;
	}
	
	public String getStringValue() {
		return this.stringValue;
	}
	
	public Type getType() {
		return this.type;
	}
}
