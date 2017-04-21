package cd.frontend.semantic;

import java.util.List;

import cd.ir.Ast;
import cd.ir.Symbol;

public class TypeChecker {
	private SymbolTable symbolTable;
	
	public TypeChecker(SymbolTable symbolTable){
		this.symbolTable = symbolTable;
	}
	
	public void check(List<Ast.ClassDecl> classDecls) {
        for (Ast.ClassDecl classDecl : classDecls) {
            StmtTypeChecker checker = new StmtTypeChecker((Symbol.ClassSymbol) symbolTable.get(classDecl.name));
            checker.visit(classDecl, null);
        }
    }

}
