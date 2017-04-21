package cd.frontend.semantic;

import cd.ir.Ast.Assign;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.ReturnStmt;
import cd.ir.Ast.VarDecl;
import cd.ir.AstVisitor;
import cd.ir.Symbol;
import cd.ir.Symbol.VariableSymbol.Kind;

public class SymbolTableFill extends AstVisitor<Symbol, Symbol.VariableSymbol.Kind>  {
	
	private SymbolTable<Symbol.TypeSymbol> symbolTable;
	
	public SymbolTableFill(SymbolTable<Symbol.TypeSymbol> symbolTable){
		this.symbolTable = symbolTable;
	}
	
	public Symbol.TypeSymbol undeclaredType(String type){
		
		return null;
	}

	@Override
	public Symbol assign(Assign ast, Kind arg) {
		// TODO Auto-generated method stub
		return super.assign(ast, arg);
	}

	@Override
	public Symbol methodDecl(MethodDecl ast, Kind arg) {
		// TODO Auto-generated method stub
		return super.methodDecl(ast, arg);
	}

	@Override
	public Symbol varDecl(VarDecl ast, Kind arg) {
		// TODO Auto-generated method stub
		return super.varDecl(ast, arg);
	}

	@Override
	public Symbol returnStmt(ReturnStmt ast, Kind arg) {
		// TODO Auto-generated method stub
		return super.returnStmt(ast, arg);
	}

}
