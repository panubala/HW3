package cd.frontend.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cd.ir.Symbol;
import cd.ir.Symbol.TypeSymbol;
import cd.ir.Symbol.VariableSymbol;

public class SymbolTable<S extends Symbol>{

		private SymbolTable <? extends Symbol> table;
		private Map<String, S> symbolTable = new HashMap<>();
		
		public SymbolTable(){
			this.table = null;
		}
		
		public SymbolTable(SymbolTable table){
			this.table = table;
		}
		
		public Symbol get(String string){
			S key = symbolTable.get(string);
			if (table != null && key == null){
				return table.get(string);
			}
			return key;
		}
		
		public Collection<S> getAllSymbols(){
			return symbolTable.values();
		}
		
		public Collection<Symbol.ClassSymbol> getAllClassSymbols(){
			Collection<S> allSymbols = symbolTable.values();
			
			ArrayList result = new ArrayList<>();

			
			for(Symbol s: allSymbols){

				if(s instanceof Symbol.ClassSymbol){
					result.add(s);
				}
			}
			

			
			return result;
		}
		
		public void put(S symbol) {
		        symbolTable.put(symbol.name, symbol);
		        
		}
		
		public void put(String name, S symbol){
			symbolTable.put(name, symbol);
		}
		
		
		public boolean containsKey(String s){
			return symbolTable.containsKey(s);
		}
		
		public boolean containsType(TypeSymbol type){
			return symbolTable.containsValue(type);
		}
		
		public void print(){
			System.out.println("=====Table:=====");
			for (String name: symbolTable.keySet()){
	            String key =name.toString();
	            String value = symbolTable.get(name).toString();  
	            System.out.println(key + " -> " + value);
			} 
			System.out.println("================");
		}
		

	
}
