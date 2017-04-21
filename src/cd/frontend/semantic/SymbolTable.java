package cd.frontend.semantic;

import cd.ir.Symbol;

public class SymbolTable<S extends Symbol>{

		private SymbolTable table;
		
		public SymbolTable(){
			this.table = null;
		}
		
		public SymbolTable(SymbolTable table){
			this.table = table;
		}

	
}
