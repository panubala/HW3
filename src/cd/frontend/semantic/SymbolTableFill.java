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

	private TypeTable globalSymbolTable;
	private HashMap<String, SymbolTable> classTables = new HashMap<>(); // Key:
																		// ClassNames
	private HashMap<String, SymbolTable> methodTables = new HashMap<>(); // Key:
																			// ClassNames
																			// +
																			// MethodName

	private SymbolTable currentScopeTable;

	public SymbolTableFill(TypeTable allTypes, HashMap<String, SymbolTable> globalClassTable,
			HashMap<String, SymbolTable> globalMethodTable) {
		this.globalSymbolTable = allTypes;
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

	@Override
	public Symbol classDecl(ClassDecl ast, Kind arg) {
		System.out.println("==Filling - ClassDecl");

		ast.sym.superClass = (Symbol.ClassSymbol) undeclaredType(ast.superClass);

		for (Ast.VarDecl varDecl : ast.fields()) {
			if (classTables.get(ast.name).containsField(varDecl.name))
				throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION); // twice
																						// same
																						// fieldName
			currentScopeTable = classTables.get(ast.name);
			Symbol.VariableSymbol field = (Symbol.VariableSymbol) visit(varDecl, Symbol.VariableSymbol.Kind.FIELD);
			// ast.sym.fields.put(field.name, field);
		}

		for (Ast.MethodDecl methodDecl : ast.methods()) {

			String fncName = methodDecl.name;

			if (classTables.get(ast.name).containsFunction(fncName)) {
				if (classTables.get(ast.name).getFunctionType(fncName).equals(methodDecl.returnType)) {
					throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION); // twice
																							// same
																							// method
				}
				if (methodTables.containsKey(ast.name + methodDecl.name)) {
					throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
				}
			}
			methodTables.put(ast.name + methodDecl.name, new SymbolTable());

			currentScopeTable = methodTables.get(ast.name + methodDecl.name);

			currentScopeTable.inClass = ast.name;

			for (String name : methodDecl.argumentNames) {
				if (currentScopeTable.parameterNames.contains(name))
					throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
				currentScopeTable.parameterNames.add(name);
			}
			
			//for (String name : classTables.get(ast.name).getAllFieldNames()){
			//}

			Symbol.MethodSymbol method = (Symbol.MethodSymbol) visit(methodDecl, null);
			// if(ast.sym.methods.containsKey(method.name))
			classTables.get(ast.name).putFunction(methodDecl.name, method.returnType);

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

		TypeSymbol returnType = (TypeSymbol) globalSymbolTable.get(ast.returnType);
		if (returnType == null) {
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE);
		}
		ast.sym.returnType = returnType;

		return ast.sym;
	}

	@Override
	public Symbol varDecl(VarDecl ast, Kind arg) {
		System.out.println("==Filling - VarDecl");
		Symbol.TypeSymbol typeSymbol = undeclaredType(ast.type);

		ast.sym = new Symbol.VariableSymbol(ast.name, typeSymbol, arg);

		if (currentScopeTable.containsField(ast.name))
			throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);
		currentScopeTable.putField(ast.name, typeSymbol);
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

			if (classDecl.name.equals("Object"))
				throw new SemanticFailure(SemanticFailure.Cause.OBJECT_CLASS_DEFINED);

			if (classTables.containsKey(classDecl.name))
				throw new SemanticFailure(SemanticFailure.Cause.DOUBLE_DECLARATION);

			classDecl.sym = new Symbol.ClassSymbol(classDecl);
			globalSymbolTable.put(classDecl.sym);
			globalSymbolTable.put(new Symbol.ArrayTypeSymbol(classDecl.sym));

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

		// Inheritence
		for (Ast.ClassDecl classDecl : classDecls) {
			if (!classDecl.superClass.equals("Object")) {
				
				SymbolTable superClass = classTables.get(classDecl.superClass);
				SymbolTable orClass = classTables.get(classDecl.name);

				orClass.extendsFrom = classDecl.superClass;

				// override Field in orClass
				for (String fieldName : superClass.getAllFieldNames()) {
					if (!orClass.containsField(fieldName))
						orClass.putField(fieldName, superClass.getFieldType(fieldName)); // insert
																							// if
																							// not
																							// there
					else if (orClass.getFieldType(fieldName).equals(superClass.getFieldType(fieldName))) { // check
																											// if
																											// same
																											// type
						orClass.putField(fieldName, superClass.getFieldType(fieldName)); // override
																							// it
					} else {
						throw new SemanticFailure(SemanticFailure.Cause.INVALID_OVERRIDE);
					}
				}

				// override Method in orClass
				for (String fncName : superClass.getAllFunctionNames()) {
					
					if (!orClass.containsFunction(fncName)) { // not in the
																// original
																// class
						orClass.putFunction(fncName, superClass.getFunctionType(fncName));

						Map<String, TypeSymbol> orFncTbl = methodTables.get(classDecl.superClass + fncName)
								.wholefunctionTable();
						Map<String, TypeSymbol> orFieldTbl = methodTables.get(classDecl.superClass + fncName)
								.wholefieldTable();

						SymbolTable newTable = new SymbolTable(orFieldTbl, orFncTbl);
						newTable.parameterNames = methodTables.get(classDecl.superClass + fncName).parameterNames;
						newTable.inClass = classDecl.name;
						methodTables.put(classDecl.name + fncName, newTable);
					} else if (!orClass.getFunctionType(fncName).equals(superClass.getFunctionType(fncName))) {
						throw new SemanticFailure(SemanticFailure.Cause.INVALID_OVERRIDE,
								"Overridden method method has not the same return type as the original");
					} else if (methodTables.get(classDecl.superClass + fncName).parameterNames
							.size() != methodTables.get(classDecl.name + fncName).parameterNames.size()) {
						
						System.out.println(classDecl.superClass + fncName);
						methodTables.get(classDecl.superClass + fncName).print();
						System.out.println(classDecl.name + fncName);
						methodTables.get(classDecl.name + fncName).print();
						
						System.out.println(methodTables.get(classDecl.superClass + fncName).parameterNames
							.size());
						System.out.println(methodTables.get(classDecl.name + fncName).parameterNames.size());
						throw new SemanticFailure(SemanticFailure.Cause.INVALID_OVERRIDE,
								"Overridden method method has not the same amount of parameters as the original has");
					} else { // Check if every Type is the same

						ArrayList<String> params = methodTables.get(classDecl.superClass + fncName).parameterNames;
						SymbolTable methTablSuper = methodTables.get(classDecl.superClass + fncName);
						SymbolTable methTablOr = methodTables.get(classDecl.name + fncName);
						
						for (int i = 0; i < params.size(); i++) {
							String nameSu = methTablSuper.parameterNames.get(i);
							String nameOr = methTablOr.parameterNames.get(i);
							
							if (!methTablSuper.getFieldType(nameSu).equals(methTablOr.getFieldType(nameOr)))
								throw new SemanticFailure(SemanticFailure.Cause.INVALID_OVERRIDE,
										"Overridden method method has not the same parameter-type as the original");
						}
					}
				}
			}
		}
	}
	

	private void inheritanceCheck() {

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
