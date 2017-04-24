package cd.frontend.semantic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cd.ir.Ast;
import cd.ir.Ast.Assign;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.ReturnStmt;
import cd.ir.Ast.VarDecl;
import cd.ir.AstVisitor;
import cd.ir.Symbol;
import cd.ir.Symbol.MethodSymbol;
import cd.ir.Symbol.TypeSymbol;
import cd.ir.Symbol.VariableSymbol.Kind;

public class SymbolTableFill extends AstVisitor<Symbol, Symbol.VariableSymbol.Kind>  {
	
	private SymbolTable<Symbol.TypeSymbol> symbolTable;
	private Set<String> classNames = new HashSet<>();
	
	public SymbolTableFill(SymbolTable<Symbol.TypeSymbol> symbolTable){
		this.symbolTable = symbolTable;
	}
	
	public Symbol.TypeSymbol undeclaredType(String type){
		System.out.println("==Filling - undeClared Type");
		Symbol.TypeSymbol typeSymbol = (TypeSymbol) symbolTable.get(type);
		
		if (typeSymbol == null) {
            throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE, "Type not found");
        }
		return typeSymbol;
	}

	//TODO:inheritence
	@Override
	public Symbol classDecl(ClassDecl ast, Kind arg) {
		System.out.println("==Filling - ClassDecl");
		// TODO Auto-generated method stub
		ast.sym.superClass = (Symbol.ClassSymbol) undeclaredType(ast.superClass);
		
		System.out.println("Passed SuperClass");
		
		//TODO:Duplicates
		for (Ast.MethodDecl methodDecl : ast.methods()) {
			Symbol.MethodSymbol method = (Symbol.MethodSymbol) visit(methodDecl, null);
			if(ast.sym.methods.containsKey(method.name))
				throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
            
			ast.sym.methods.put(method.name, method);
            System.out.println("Method putted");
		}
		
		System.out.println("Added MethodDecl");
		
		//TODO: Duplicates
		for (Ast.VarDecl varDecl : ast.fields()) {
			Symbol.VariableSymbol field = (Symbol.VariableSymbol) visit(varDecl, Symbol.VariableSymbol.Kind.FIELD);
			ast.sym.fields.put(field.name, field);
		}
		return ast.sym;
	}

	@Override
	public Symbol assign(Assign ast, Kind arg) {
		System.out.println("==Filling - Assign");
		
		
		// TODO Auto-generated method stub
		return super.assign(ast, arg);
	}

	@Override
	public Symbol methodDecl(MethodDecl ast, Kind arg) {
		System.out.println("==Filling - MethodDecl");
		visit(ast.decls(), arg);
		ast.sym = new MethodSymbol(ast);
		return ast.sym;
	}

	@Override
	public Symbol varDecl(VarDecl ast, Kind arg) {
		System.out.println("==Filling - VarDecl");
		Symbol.TypeSymbol typeSymbol = undeclaredType(ast.type);
		ast.sym = new Symbol.VariableSymbol(ast.name, typeSymbol,arg); //TODO do we have to set ast.sym? or can we just simply retrun new Symbol
		symbolTable.put(ast.name, typeSymbol);
		return ast.sym;
	}

	@Override
	public Symbol returnStmt(ReturnStmt ast, Kind arg) {
		System.out.println("==Filling - ReturnStmt");
		// TODO Auto-generated method stub
		return super.returnStmt(ast, arg);
	}
	
	public void fillTable(List<Ast.ClassDecl> classDecls) {
		
		System.out.println("Number of classes: " + classDecls.size());
		
		System.out.println("Putting Built-in");
		symbolTable.put(Symbol.TypeSymbol.PrimitiveTypeSymbol.intType);
		symbolTable.put(Symbol.TypeSymbol.PrimitiveTypeSymbol.booleanType);
		symbolTable.put(Symbol.TypeSymbol.PrimitiveTypeSymbol.voidType);
		symbolTable.put(new Symbol.ArrayTypeSymbol(Symbol.TypeSymbol.PrimitiveTypeSymbol.intType));
		symbolTable.put(new Symbol.ArrayTypeSymbol(Symbol.TypeSymbol.PrimitiveTypeSymbol.booleanType));
		symbolTable.put(Symbol.TypeSymbol.ClassSymbol.objectType);
		symbolTable.put(Symbol.TypeSymbol.ClassSymbol.nullType);
		symbolTable.put(new Symbol.ArrayTypeSymbol(Symbol.TypeSymbol.ClassSymbol.objectType));
		
		for (Ast.ClassDecl classDecl : classDecls) {
			
			if(classNames.contains(classDecl.name))
				throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
			
			classNames.add(classDecl.name);
			classDecl.sym = new Symbol.ClassSymbol(classDecl);
			symbolTable.put(classDecl.sym);
			//symbolTable.put(new Symbol.ArrayTypeSymbol(classDecl.sym));
		}
		
		for (Ast.ClassDecl classDecl : classDecls) {
			System.out.println("Filling table...");
            visit(classDecl, null);
            System.out.println("Returned visit");
        }
		
		System.out.println("Table filled");
		
	}

}
