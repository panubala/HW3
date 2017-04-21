package cd.frontend.semantic;

import java.util.HashMap;
import java.util.Map;

import cd.ir.Symbol;

public class SymbolTable<S extends Symbol>{

		private SymbolTable table;
		private Map<String, S> symbolTable = new HashMap<>();
		
		public SymbolTable(){
			this.table = null;
		}
		
		public SymbolTable(SymbolTable table){
			this.table = table;
		}
		
		public Symbol get(String string){
			if (table != null){
				return table.get(string);
			}
			S key = symbolTable.get(string);
			
			return key;
			
		}
		

	
}
