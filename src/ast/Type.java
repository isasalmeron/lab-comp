/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

abstract public class Type {

    public Type( String name ) {
        this.name = name;
    }

    public static Type booleanType = new TypeBoolean();
    public static Type intType = new TypeInt();
    public static Type stringType = new TypeString();
    public static Type undefinedType = new TypeUndefined();
  //nao sei se precisa trocar pra nilType, tanto a variavel, função e nome do arquivo
    public static Type nullType = new TypeNull(); 
    
    public String getName() {
        return name;
    }

    abstract public String getCname();

    private String name;
}
