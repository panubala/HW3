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

	
	private Map<String, TypeSymbol> symbolTable = new HashMap<>();

	public SymbolTable() {
		this.symbolTable = symbolTable;
	}

	public SymbolTable(Map<String, TypeSymbol> table) {
		this.symbolTable = table;
	}
	
	public Map<String, TypeSymbol> wholeTable(){
		return symbolTable;
	}

	public TypeSymbol get(String string) {
		TypeSymbol key = symbolTable.get(string);

		return key;
	}
	

	public Collection<TypeSymbol> getAllSymbols() {
		return symbolTable.values();
	}
	
	public Set<String> getAllNames() {
		return symbolTable.keySet();
	}

	// public Set<Pair<String>> parameters = new HashSet<>();
	public ArrayList<String> parameterNames = new ArrayList<>();
	
	public String inClass;

	public Collection<Symbol.ClassSymbol> getAllClassSymbols() {
		Collection<TypeSymbol> allSymbols = symbolTable.values();
		ArrayList result = new ArrayList<>();
		for (Symbol s : allSymbols) {
			if (s instanceof Symbol.ClassSymbol) {
				result.add(s);
			}
		}
		return result;
	}

	public void put(TypeSymbol symbol) {
		symbolTable.put(symbol.name, symbol);

	}

	public void put(String name, TypeSymbol symbol) {
		symbolTable.put(name, symbol);
	}

	public boolean containsKey(String s) {
		return symbolTable.containsKey(s);
	}

	public boolean containsType(TypeSymbol type) {
		return symbolTable.containsValue(type);
	}

	public void print() {
		System.out.println("==Table: (inClass " + inClass + ")=====");
		System.out.println("=====Arg:======");
		// for(Pair p: parameters){
		// System.out.println(p.a + ": " + p.b);
		// }
		for (String par : parameterNames) {
			System.out.print(par + ", ");
		}

		System.out.println("\n----------------");

		for (String name : symbolTable.keySet()) {
			String key = name.toString();
			String value = symbolTable.get(name).toString();
			System.out.println(key + " -> " + value);
		}
		System.out.println("================");
	}

}
