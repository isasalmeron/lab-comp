/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package ast;
/*
 * Cianeto Class
 */
public class TypeCianetoClass extends Type {

   public TypeCianetoClass( String name ) {
      super(name);
   }

   @Override
   public String getJavaName() {
      return getName();
   }

   private String name;
   private TypeCianetoClass superclass;
   // private FieldList fieldList;
   // private MethodList publicMethodList, privateMethodList;
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos
}
