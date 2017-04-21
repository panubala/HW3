package cd.frontend.semantic;

import java.util.List;

import cd.Main;
import cd.ToDoException;
import cd.ir.Ast.ClassDecl;
import cd.ir.Symbol;
import cd.ir.Symbol.ClassSymbol;

public class SemanticAnalyzer {
	
	public final Main main;
	private SymbolTable<Symbol.TypeSymbol> globalSymbolTable = new SymbolTable<>();
	
	public SemanticAnalyzer(Main main) {
		this.main = main;
	}
	
	public void check(List<ClassDecl> classDecls) throws SemanticFailure {
		{
			System.out.println("checking...");
			new TypeChecker(globalSymbolTable);
			
			Symbol.ClassSymbol mainClassSymbol = (Symbol.ClassSymbol) globalSymbolTable.get("Main");
			
			System.out.println("Going to check Start Point...");
			//Start Point
			if (mainClassSymbol == null){
				System.out.println("No Main Class");
				throw new SemanticFailure(SemanticFailure.Cause.INVALID_START_POINT, "No Main Class found");
			}else if (mainClassSymbol.getMethod("main") == null) {
				System.out.println("No Main Method");
				throw new SemanticFailure(SemanticFailure.Cause.INVALID_START_POINT, "No Main Method found");
			}else if (mainClassSymbol.getMethod("main").parameters.size() != 0) {
				System.out.println("Should be no Parameters in Main Method");
				throw new SemanticFailure(SemanticFailure.Cause.INVALID_START_POINT, "Should be no Parameters in Main Method");
			}
			
			System.out.println("Done with Start Point");
		}
	}

}
