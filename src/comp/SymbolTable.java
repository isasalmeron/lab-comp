/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package comp;

import java.util.Hashtable;

import ast.IdList;

public class SymbolTable {
	private Hashtable<String, Object> globalTable, localTable;
	
	public SymbolTable() {
		globalTable = new Hashtable<String, Object>();
		localTable = new Hashtable<String, Object>();
	}
	
	public void putInGlobal(final String key, final Object value) {
		globalTable.put(key, value);
	}
	
	public void putInLocal(final String key, final Object value) {
		localTable.put(key, value);
	}
	
	public void putAllInLocal(IdList variables) {
		for (String variable : variables.getIdentifiers()) {
			this.putInLocal(variable, variable);
		}
	}
	
	public Object get(final Object key) {
		Object result;
		
		if ((result = localTable.get(key)) != null) {
			return result;
		}
		
		return globalTable.get(key);
	}
	
	public void clearLocal() {
		localTable.clear();
	}
}
