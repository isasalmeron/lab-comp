/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;

public class TypeInt extends Type {
    
    public TypeInt() {
        super("int");
    }
    
   public String getCname() {
      return "int";
   }

}
