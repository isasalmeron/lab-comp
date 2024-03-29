/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

abstract public class Type {
	
	private String name;

    public Type(String name) {
        this.name = name;
    }

    public static Type booleanType = new TypeBoolean();
    public static Type intType = new TypeInt();
    public static Type stringType = new TypeString();
    public static Type undefinedType = new TypeUndefined();
    public static Type nilType = new TypeNil(); 
    public static Type cianetoClassType = new TypeCianetoClass(null);
    
    public String getName() {
        return this.name;
    }
}
