package cd.frontend.semantic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cd.Main;
import cd.ir.Ast.ClassDecl;
import cd.ir.Symbol;

public class SemanticAnalyzer {
	
	public final Main main;	
	private SymbolTable<Symbol.TypeSymbol> globalSymbolTable = new SymbolTable<>();
	private HashMap<String, SymbolTable> globalClassTable = new HashMap<>();
	private HashMap<String, SymbolTable> globalMethodTable = new HashMap<>();
	
	public SemanticAnalyzer(Main main) {
		this.main = main;
	}
	
	public void check(List<ClassDecl> classDecls) throws SemanticFailure {
		{
			System.out.println("checking...");
			
			//new SymbolTableFill(globalScopeSymbolTable).fillTable(classDecls);		
			new SymbolTableFill(globalSymbolTable, globalClassTable, globalMethodTable).fillTable(classDecls);		
			
			////Printing:
			System.out.println("Global Table:");
			System.out.println("-------------");
			globalSymbolTable.print();
			System.out.println();
			
			System.out.println("Class Table:");
			System.out.println("-------------");
			Iterator it = globalClassTable.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println("-----"+pair.getKey()+"-----");
		        ((SymbolTable) pair.getValue()).print();
		    }
			System.out.println();
		    
		    System.out.println("Method Table:");
			System.out.println("-------------");
			it = globalMethodTable.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println("-----"+pair.getKey()+"-----");
		        ((SymbolTable) pair.getValue()).print();
		    }
			//////////////////////////////////////////////////////////
			
			Symbol.MethodSymbol mainMethodSymbol = (Symbol.MethodSymbol) globalClassTable.get("Main").get("main");
			
			System.out.println("Going to check Start Point...");
			//Start Point
			if (!globalClassTable.containsKey("Main")){
				System.out.println("No Main Class");
				throw new SemanticFailure(SemanticFailure.Cause.INVALID_START_POINT, "No Main Class found");
			}else if (!globalMethodTable.containsKey("Mainmain")) {
				System.out.println("No Main Method");
				throw new SemanticFailure(SemanticFailure.Cause.INVALID_START_POINT, "No Main Method found");
			}else if (mainMethodSymbol.parameters.size() != 0) {
				System.out.println("Should be no Parameters in Main Method");
				throw new SemanticFailure(SemanticFailure.Cause.INVALID_START_POINT, "Should be no Parameters in Main Method");
			}
			
			System.out.println("Done with Start Point");
			
			
			globalSymbolTable.print();
			
			
			new TypeChecker(globalSymbolTable, globalClassTable, globalMethodTable).check(classDecls);
		}
	}

}
