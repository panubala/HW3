package cd.frontend.semantic;

import java.util.List;

import cd.ir.Ast;
import cd.ir.Symbol;

public class TypeChecker { //TODO why TypeChecker? Does it only check types?
	private SymbolTable symbolTable;
	
	public StmtTypeChecker stmtChecker;
	
	public TypeChecker(SymbolTable symbolTable){
		this.symbolTable = symbolTable;
	}
	
	public void check(List<Ast.ClassDecl> classDecls) {
        for (Ast.ClassDecl classDecl : classDecls) {
    		this.stmtChecker = new StmtTypeChecker((Symbol.ClassSymbol) symbolTable.get(classDecl.name), symbolTable);
            stmtChecker.visit(classDecl, null);
        }
    }

}
