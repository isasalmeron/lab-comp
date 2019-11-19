/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package comp;

import java.util.Hashtable;

public class SymbolTable {
	private Hashtable<String, Object> globalTable, classTable, localTable;
	
	public SymbolTable() {
		globalTable = new Hashtable<String, Object>();
		classTable = new Hashtable<String, Object>();
		localTable = new Hashtable<String, Object>();
	}
	
	public void putInGlobal(final String key, final Object value) {
		globalTable.put(key, value);
	}
	
	public void putInClass(final String key, final Object value) {
		classTable.put(key, value);
	}
	
	public void putInLocal(final String key, final Object value) {
		localTable.put(key, value);
	}
	
	public Object get(final Object key) {
		Object result;
		
		if ((result = this.getInClass(key)) != null) {
			return result;
		}
		
		return globalTable.get(key);
	}
	
	public Object getInLocal(final Object key) {
		return localTable.get(key);
	}
	
	public Object getInGlobal(final Object key) {
		return this.globalTable.get(key);
	}
	
	public Object getInClass(final Object key) {	
		return this.classTable.get(key);
	}
	
	public Object getInCurrentScope(final Object key) {
		Object result;
		
		if ((result = localTable.get(key)) != null) {
			return result;
		}
		
		return this.classTable.get(key);
	}
	
	public void clearLocal() {
		localTable.clear();
	}
	
	public void clearClass() {
		classTable.clear();
	}
}
