package cd.frontend.semantic;

import java.util.HashMap;
import java.util.List;

import cd.ir.Ast;
import cd.ir.Symbol;

public class TypeChecker { // TODO why TypeChecker? Does it only check types?

	private StmtTypeChecker stmtChecker;
	static public TypeTable symbolTable;
	static public HashMap<String, SymbolTable> classTable; // Key: "className"
	static public HashMap<String, SymbolTable> methodTable; // Key: "className"
															// + "methodName"

	public TypeChecker(TypeTable allTypes, HashMap<String, SymbolTable> classTable,
			HashMap<String, SymbolTable> methodTable) {
		TypeChecker.symbolTable = allTypes;
		TypeChecker.classTable = classTable;
		TypeChecker.methodTable = methodTable;
	}

	public void check(List<Ast.ClassDecl> classDecls) {
		for (Ast.ClassDecl classDecl : classDecls) {
			this.stmtChecker = new StmtTypeChecker((Symbol.ClassSymbol) symbolTable.get(classDecl.name));
			stmtChecker.visit(classDecl, null);
		}
	}

}
