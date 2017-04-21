package cd.frontend.semantic;

import cd.ir.Ast;
import cd.ir.Ast.Assign;
import cd.ir.Ast.BuiltInWrite;
import cd.ir.Ast.BuiltInWriteln;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.IfElse;
import cd.ir.Ast.MethodCall;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.Nop;
import cd.ir.Ast.ReturnStmt;
import cd.ir.Ast.Seq;
import cd.ir.Ast.VarDecl;
import cd.ir.Ast.WhileLoop;
import cd.ir.AstVisitor;
import cd.ir.Symbol;

//TODO Void, Void?
public class StmtTypeChecker extends AstVisitor<Void, Void> {
	private SymbolTable globalSymboltable;
	private SymbolTable <Symbol.VariableSymbol> localSymbolTable;
	private ExprTypeChecker etc;

	@Override
	public Void visit(Ast ast, Void arg) {
		// TODO Auto-generated method stub
		return super.visit(ast, arg);
	}

	@Override
	public Void visitChildren(Ast ast, Void arg) {
		// TODO Auto-generated method stub
		return super.visitChildren(ast, arg);
	}

	@Override
	public Void assign(Assign ast, Void arg) {
		// TODO Auto-generated method stub
		return super.assign(ast, arg);
	}

	@Override
	public Void builtInWrite(BuiltInWrite ast, Void arg) {
		// TODO Auto-generated method stub
		return super.builtInWrite(ast, arg);
	}

	@Override
	public Void builtInWriteln(BuiltInWriteln ast, Void arg) {
		// TODO Auto-generated method stub
		return super.builtInWriteln(ast, arg);
	}

	@Override
	public Void classDecl(ClassDecl ast, Void arg) {
		// TODO Auto-generated method stub
		return super.classDecl(ast, arg);
	}

	@Override
	public Void methodDecl(MethodDecl ast, Void arg) {
		// TODO Auto-generated method stub
		return super.methodDecl(ast, arg);
	}

	@Override
	public Void varDecl(VarDecl ast, Void arg) {
		// TODO Auto-generated method stub
		return super.varDecl(ast, arg);
	}

	@Override
	public Void ifElse(IfElse ast, Void arg) {
		// TODO Auto-generated method stub
		
		Symbol.TypeSymbol conditionType = etc.visit(ast.condition(), localSymbolTable);
		
		if (!conditionType.equals(Symbol.PrimitiveTypeSymbol.booleanType)) {
            throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "ifelse requires cond to be of type boolean");
        }
		
		visit(ast.then(), arg);
		
		return visit(ast.otherwise(), arg) ;
	}

	@Override
	public Void returnStmt(ReturnStmt ast, Void arg) {
		// TODO Auto-generated method stub
		return super.returnStmt(ast, arg);
	}

	@Override
	public Void methodCall(MethodCall ast, Void arg) {
		// TODO Auto-generated method stub
		return super.methodCall(ast, arg);
	}

	@Override
	public Void nop(Nop ast, Void arg) {
		// TODO Auto-generated method stub
		return super.nop(ast, arg);
	}

	@Override
	public Void seq(Seq ast, Void arg) {
		// TODO Auto-generated method stub
		return super.seq(ast, arg);
	}

	@Override
	public Void whileLoop(WhileLoop ast, Void arg) {
		// TODO Auto-generated method stub
		return super.whileLoop(ast, arg);
	}
	
	

}
