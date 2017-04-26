package cd.frontend.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import cd.ir.Symbol;
import cd.ir.Symbol.ClassSymbol;
import cd.ir.Symbol.TypeSymbol;

public class TypeTable {

	public HashMap<String, TypeSymbol> types = new HashMap<>();

	public void put(TypeSymbol symbol) {
		types.put(symbol.name, symbol);
	}

	public TypeSymbol get(String type) {
		return types.get(type);
	}

	public Collection<TypeSymbol> getAllSymbols() {
		return types.values();
	}

	public boolean contains(String type) {
		return types.containsKey(type);
	}

	public void print() {
		for (String name : types.keySet()) {
			String value = types.get(name).toString();
			System.out.println(name + " -> " + value);
		}
	}

	public Collection<ClassSymbol> getAllClassSymbols() {
		Collection<TypeSymbol> allSymbols = getAllSymbols();
		ArrayList<ClassSymbol> result = new ArrayList<>();
		for (Symbol s : allSymbols) {
			if (s instanceof Symbol.ClassSymbol) {
				result.add((Symbol.ClassSymbol) s);
			}
		}
		return result;
	}

}
