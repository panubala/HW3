package cd.frontend.semantic;

import java.util.HashMap;
import java.util.Map;

import cd.ir.Symbol;

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
		
		public void put(S symbol) {
		        symbolTable.put(symbol.name, symbol);
		}
		
		public void put(String name, S symbol){
			symbolTable.put(name, symbol);
		}
		
		public boolean containsKey(String s){
			return symbolTable.containsKey(s);
		}
		

	
}
