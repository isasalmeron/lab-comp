/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package comp;

import java.util.Hashtable;
import java.util.List;

import ast.Variable;

public class SymbolTable {
	private Hashtable<String, Object> globalTable, classTable, localTable, methodTable;
	
	public SymbolTable() {
		globalTable = new Hashtable<String, Object>();
		classTable = new Hashtable<String, Object>();
		localTable = new Hashtable<String, Object>();
		methodTable = new Hashtable<String, Object>();
	}
	
	public void putInGlobal(final String key, final Object value) {
		globalTable.put(key, value);
	}
	
	public void putInClass(final String key, final Object value) {
		classTable.put(key, value);
	}
	
	public void addClass(final String key, final Object value) {
		classTable.put(key, value);
	}
	
	public void addMethod(final String key, final Object value) {
		methodTable.put(key, value);
	}
	
	public void putInLocal(final String key, final Object value) {
		localTable.put(key, value);
	}
	
	public void putAllInLocal(List<Variable> variables) {
		for (Variable variable : variables) {
			this.putInLocal(variable.getName(), variable);
		}
	}
	
	public void putAllInGlobal(List<Variable> variables) {
		for (Variable variable : variables) {
			this.putInGlobal(variable.getName(), variable);
		}
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
	
	public Object getClass(final Object key) {
		Object result;
		
		if ((result = localTable.get(key)) != null) {
			return result;
		}
		
		return this.classTable.get(key);
	}
	
	public Object getMethod(final Object key) {
		return this.methodTable.get(key);
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
