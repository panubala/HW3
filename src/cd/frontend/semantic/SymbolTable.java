package cd.frontend.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cd.ir.Symbol;
import cd.ir.Symbol.TypeSymbol;

public class SymbolTable{

	
	private Map<String, TypeSymbol> fieldTable = new HashMap<>();
	private Map<String, TypeSymbol> functionTable = new HashMap<>();

	public SymbolTable() {
	}

	public SymbolTable(Map<String, TypeSymbol> fieldTable, Map<String, TypeSymbol> functionTable) {
		this.fieldTable = fieldTable;
		this.functionTable = functionTable;
	}
	
	public Map<String, TypeSymbol> wholefunctionTable(){
		return functionTable;
	}
	
	public Map<String, TypeSymbol> wholefieldTable(){
		return fieldTable;
	}

	public TypeSymbol getFunctionType(String fncName) {
		TypeSymbol type = functionTable.get(fncName);
		return type;
	}
	
	public TypeSymbol getFieldType(String fncName) {
		TypeSymbol type = fieldTable.get(fncName);
		return type;
	}
	
	public Collection<TypeSymbol> getAllFunctionType() {
		return functionTable.values();
	}
	
	public Collection<TypeSymbol> getAllFieldType() {
		return fieldTable.values();
	}
	
	public Set<String> getAllFunctionNames() {
		return functionTable.keySet();
	}
	
	public Set<String> getAllFieldNames() {
		return fieldTable.keySet();
	}
	
	
	public Collection<TypeSymbol> getAllTypeSymbols() {
		Collection<TypeSymbol> c = fieldTable.values();
		c.addAll(functionTable.values());
		return c;
	}
	
	public Set<String> getAllNames() {
		Set<String> c = fieldTable.keySet();
		c.addAll(functionTable.keySet());
		return c;
	}
	
	public String extendsFrom;

	// public Set<Pair<String>> parameters = new HashSet<>();
	public ArrayList<String> parameterNames = new ArrayList<>();
	
	public String inClass;

	public Collection<Symbol.ClassSymbol> getAllClassSymbols() {
		Collection<TypeSymbol> allSymbols = getAllTypeSymbols();
		ArrayList result = new ArrayList<>();
		for (Symbol s : allSymbols) {
			if (s instanceof Symbol.ClassSymbol) {
				result.add(s);
			}
		}
		return result;
	}

	public void putFunction(String fncName, TypeSymbol symbol) {
		functionTable.put(fncName, symbol);
	}
	
	public void putField(String fieldName, TypeSymbol symbol) {
		fieldTable.put(fieldName, symbol);
	}

	public boolean containsFunction(String fncName) {
		return functionTable.containsKey(fncName);
	}
	
	public boolean containsField(String fieldName) {
		return fieldTable.containsKey(fieldName);
	}
	
	public boolean containss(String name) {
		return containsField(name) || containsFunction(name); 
	}

//	public boolean containsType(TypeSymbol type) {
//		return symbolTable.containsValue(type);
//	}

	public void print() {
		System.out.println("==Table: (inClass " + inClass + ")=====");
		System.out.println("==extends from " + extendsFrom + "====");
		System.out.println("=====Arg:======");
		// for(Pair p: parameters){
		// System.out.println(p.a + ": " + p.b);
		// }
		for (String par : parameterNames) {
			System.out.print(par + ", ");
		}

		System.out.println("\n------fields:-------");

		for (String name : getAllFieldNames()) {
			String value = fieldTable.get(name).toString();
			System.out.println(name + " -> " + value);
		}
		System.out.println("--------functions:-----");
		for (String name : getAllFunctionNames()) {
			String value = functionTable.get(name).toString();
			System.out.println(name + " -> " + value);
		}
		System.out.println("================");
	}

	//public boolean hasReturnStmt;

}
