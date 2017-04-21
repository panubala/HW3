package cd.frontend.semantic;

import java.util.List;

import cd.ir.Ast;
import cd.ir.Ast.Assign;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.ReturnStmt;
import cd.ir.Ast.VarDecl;
import cd.ir.AstVisitor;
import cd.ir.Symbol;
import cd.ir.Symbol.TypeSymbol;
import cd.ir.Symbol.VariableSymbol.Kind;

public class SymbolTableFill extends AstVisitor<Symbol, Symbol.VariableSymbol.Kind>  {
	
	private SymbolTable<Symbol.TypeSymbol> symbolTable;
	
	public SymbolTableFill(SymbolTable<Symbol.TypeSymbol> symbolTable){
		this.symbolTable = symbolTable;
	}
	
	public Symbol.TypeSymbol undeclaredType(String type){
		
		Symbol.TypeSymbol typeSymbol = (TypeSymbol) symbolTable.get(type);
		
		if (typeSymbol == null) {
            throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE, "Type not found");
        }
		return typeSymbol;
	}

	//TODO:inheritence
	@Override
	public Symbol classDecl(ClassDecl ast, Kind arg) {
		// TODO Auto-generated method stub
		ast.sym.superClass = (Symbol.ClassSymbol) undeclaredType(ast.superClass);
		
		//TODO:Duplicates
		for (Ast.MethodDecl methodDecl : ast.methods()) {
			Symbol.MethodSymbol method = (Symbol.MethodSymbol) visit(methodDecl, null);
            ast.sym.methods.put(method.name, method);
		}
		
		//TODO: Duplicates
		for (Ast.VarDecl varDecl : ast.fields()) {
			Symbol.VariableSymbol field = (Symbol.VariableSymbol) visit(varDecl, Symbol.VariableSymbol.Kind.FIELD);
			ast.sym.fields.put(field.name, field);
		}
		return ast.sym;
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
		Symbol.TypeSymbol typeSymbol = undeclaredType(ast.type);
		ast.sym = new Symbol.VariableSymbol(ast.name, typeSymbol,arg);
		return ast.sym;
	}

	@Override
	public Symbol returnStmt(ReturnStmt ast, Kind arg) {
		// TODO Auto-generated method stub
		return super.returnStmt(ast, arg);
	}
	
	public void fillTable(List<Ast.ClassDecl> classDecls) {
		for (Ast.ClassDecl classDecl : classDecls) {
            visit(classDecl, null);
        }
		
	}

}
