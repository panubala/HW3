package cd.frontend.semantic;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cd.ir.Ast;
import cd.ir.Ast.Assign;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.MethodCall;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.ReturnStmt;
import cd.ir.Ast.VarDecl;
import cd.ir.AstVisitor;
import cd.ir.Symbol;
import cd.ir.Symbol.MethodSymbol;
import cd.ir.Symbol.TypeSymbol;
import cd.ir.Symbol.VariableSymbol.Kind;
import cd.util.Pair;

public class SymbolTableFill extends AstVisitor<Symbol, Symbol.VariableSymbol.Kind> {

	private SymbolTable globalSymbolTable;
	private HashMap<String, SymbolTable> classTables = new HashMap<>(); // Key:
																		// ClassNames
	private HashMap<String, SymbolTable> methodTables = new HashMap<>(); // Key:
																			// ClassNames
																			// +
																			// MethodName

	private SymbolTable currentScopeTable;

	public SymbolTableFill(SymbolTable symbolTable, HashMap<String, SymbolTable> globalClassTable,
			HashMap<String, SymbolTable> globalMethodTable) {
		this.globalSymbolTable = symbolTable;
		this.classTables = globalClassTable;
		this.methodTables = globalMethodTable;
	}

	public Symbol.TypeSymbol undeclaredType(String type) {
		System.out.println("==Filling - undeClared Type");
		Symbol.TypeSymbol typeSymbol = (TypeSymbol) globalSymbolTable.get(type);

		if (typeSymbol == null) {
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE, "Type not found");
		}
		return typeSymbol;
	}

	// TODO:inheritence
	@Override
	public Symbol classDecl(ClassDecl ast, Kind arg) {
		System.out.println("==Filling - ClassDecl");
		// TODO Auto-generated method stub
		ast.sym.superClass = (Symbol.ClassSymbol) undeclaredType(ast.superClass);

		System.out.println("Passed SuperClass");

		// TODO: Duplicates
		for (Ast.VarDecl varDecl : ast.fields()) {
			if (classTables.get(ast.name).containsKey(varDecl.name))
				throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);

			currentScopeTable = classTables.get(ast.name);
			Symbol.VariableSymbol field = (Symbol.VariableSymbol) visit(varDecl, Symbol.VariableSymbol.Kind.FIELD);

			// ast.sym.fields.put(field.name, field);

		}

		// TODO:Duplicates
		for (Ast.MethodDecl methodDecl : ast.methods()) {

			if (classTables.get(ast.name).containsKey(methodDecl.name)){
				if (classTables.get(ast.name).containsType(globalSymbolTable.get(methodDecl.returnType))) {
					throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
				}
				if (methodTables.containsKey(ast.name + methodDecl.name)){
					throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
				}
			}
			methodTables.put(ast.name + methodDecl.name, new SymbolTable());

			currentScopeTable = methodTables.get(ast.name + methodDecl.name);

			currentScopeTable.inClass = ast.name;
			// Add parameters of method
			// for(int i=0; i<methodDecl.argumentNames.size();i++){
			// Pair p = new Pair<>(methodDecl.argumentNames.get(i),
			// methodDecl.argumentTypes.get(i));
			// currentScopeTable.parameters.add(p);
			// }

			for (String name : methodDecl.argumentNames) {
				if(currentScopeTable.parameterNames.contains(name))
					throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
				currentScopeTable.parameterNames.add(name);
			}

			Symbol.MethodSymbol method = (Symbol.MethodSymbol) visit(methodDecl, null);
			// if(ast.sym.methods.containsKey(method.name))
			classTables.get(ast.name).put(methodDecl.name, method.returnType);

			// ast.sym.methods.put(method.name, method);
			System.out.println("Method putted");
		}

		System.out.println("Added MethodDecl");

		// TODO: Duplicates
		// for (Ast.VarDecl varDecl : ast.fields()) {
		// Symbol.VariableSymbol field = (Symbol.VariableSymbol) visit(varDecl,
		// Symbol.VariableSymbol.Kind.FIELD);
		// ast.sym.fields.put(field.name, field);
		// }

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

		// Add parameters into the table
		for (int i = 0; i < ast.argumentNames.size(); i++) {
			visit(new VarDecl(ast.argumentTypes.get(i), ast.argumentNames.get(i)), arg);
		}
		visit(ast.decls(), arg);

		ast.sym = new MethodSymbol(ast);

		ast.sym.returnType = (TypeSymbol) globalSymbolTable.get(ast.returnType); // TODO
																					// check
																					// if
																					// type
																					// exists
		return ast.sym;
	}

	@Override
	public Symbol varDecl(VarDecl ast, Kind arg) {
		System.out.println("==Filling - VarDecl");
		Symbol.TypeSymbol typeSymbol = undeclaredType(ast.type);

		ast.sym = new Symbol.VariableSymbol(ast.name, typeSymbol, arg);

		System.out.println(typeSymbol.name);
		currentScopeTable.put(ast.name, typeSymbol);
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
		globalSymbolTable.put(Symbol.TypeSymbol.PrimitiveTypeSymbol.intType);
		globalSymbolTable.put(Symbol.TypeSymbol.PrimitiveTypeSymbol.booleanType);
		globalSymbolTable.put(Symbol.TypeSymbol.PrimitiveTypeSymbol.voidType);
		globalSymbolTable.put(new Symbol.ArrayTypeSymbol(Symbol.TypeSymbol.PrimitiveTypeSymbol.intType));
		globalSymbolTable.put(new Symbol.ArrayTypeSymbol(Symbol.TypeSymbol.PrimitiveTypeSymbol.booleanType));
		globalSymbolTable.put(Symbol.TypeSymbol.ClassSymbol.objectType);
		globalSymbolTable.put(Symbol.TypeSymbol.ClassSymbol.nullType);
		globalSymbolTable.put(new Symbol.ArrayTypeSymbol(Symbol.TypeSymbol.ClassSymbol.objectType));

		for (Ast.ClassDecl classDecl : classDecls) {

			if (classTables.containsKey(classDecl.name))
				throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);

			classDecl.sym = new Symbol.ClassSymbol(classDecl);
			globalSymbolTable.put(classDecl.sym);

			classTables.put(classDecl.name, new SymbolTable());
			// symbolTable.put(new Symbol.ArrayTypeSymbol(classDecl.sym));
		}

		for (Ast.ClassDecl classDecl : classDecls) {
			System.out.println("Filling table...");
			visit(classDecl, null);
			System.out.println("Returned visit");
		}

		System.out.println("Table filled");

		inheritanceCheck();
		
		
		//Inheritence
		for (Ast.ClassDecl classDecl : classDecls) {
			if (!classDecl.superClass.equals("Object")) {

				SymbolTable superClass = classTables.get(classDecl.superClass);
				SymbolTable orClass = classTables.get(classDecl.name);
				
				orClass.extendsFrom = classDecl.superClass;
				
				for (String name : superClass.getAllNames()) { //name: all Vari/Field in superClass
					if (!orClass.containsKey(name) || orClass.get(name).equals(superClass.get(name))) { //Does not have super method or have it but same type
						orClass.put(name, superClass.get(name));
						if (methodTables.containsKey(classDecl.superClass + name)) {
							if(!methodTables.containsKey(classDecl.name + name)) {
								SymbolTable newTable = new SymbolTable(methodTables.get(classDecl.superClass + name).wholeTable());
								newTable.inClass = classDecl.name;
								methodTables.put(classDecl.name + name, newTable);
							}
						}
					} else{
						throw new SemanticFailure(SemanticFailure.Cause.INVALID_OVERRIDE);
					}
				}
			}
		}

	}

	void inheritanceCheck() {

		System.out.println("==Inheritance check");

		Collection<Symbol.ClassSymbol> classSyms = globalSymbolTable.getAllClassSymbols();

		for (Symbol.ClassSymbol classSym : classSyms) {
			Set<String> checked = new HashSet<>();

			Symbol.ClassSymbol current = classSym;

			while (current.superClass != null) {

				if (checked.contains(current.name)) {
					throw new SemanticFailure(SemanticFailure.Cause.CIRCULAR_INHERITANCE, "Circular Inheritance ",
							current.name);
				}

				checked.add(current.name);

				current = current.superClass;
			}
		}

	}

}
