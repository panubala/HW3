package cd.frontend.semantic;

import java.util.Map;

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
import cd.ir.Symbol.ClassSymbol;

//TODO Void, Void?
public class StmtTypeChecker extends AstVisitor<Void, Void> {
	private SymbolTable globalSymboltable;
	private SymbolTable <Symbol.VariableSymbol> localSymbolTable;
	private ExprTypeChecker etc;
	private Symbol.MethodSymbol currentMethod;
	private Map<String, Symbol.MethodSymbol> methods;
	
	public  StmtTypeChecker(Symbol.ClassSymbol classSymbol) {
		// TODO Auto-generated constructor stub
		this.methods = classSymbol.methods;
	}

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
		Symbol.TypeSymbol leftType = etc.visit(ast.left(), localSymbolTable);
        Symbol.TypeSymbol rightType = etc.visit(ast.right(), localSymbolTable);
        
        
        if (!rightType.isSubType(leftType)) {
            throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "Assignment must have compatible types.");
        }
        
		return arg;
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
		currentMethod = methods.get(ast.name);
		localSymbolTable = new SymbolTable<>();
		
		Void result = visit(ast, null);
		
		currentMethod = null;
		localSymbolTable = null;
		
		return result;
	}

	@Override
	public Void varDecl(VarDecl ast, Void arg) {
		// TODO Auto-generated method stub
		
		return super.varDecl(ast, arg);
	}

	@Override
	public Void ifElse(IfElse ast, Void arg) {
		
		Symbol.TypeSymbol conditionType = etc.visit(ast.condition(), localSymbolTable);
		
		if (!conditionType.equals(Symbol.PrimitiveTypeSymbol.booleanType)) {
            throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "ifelse requires condition to be of type boolean");
        }
		
		visit(ast.then(), arg);
		
		return visit(ast.otherwise(), arg) ;
	}

	@Override
	public Void returnStmt(ReturnStmt ast, Void arg) {
		ast.arg().type = etc.visit(ast.arg(), localSymbolTable);
		 if (!ast.arg().type.isSubType(currentMethod.returnType)) {
	            throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "ReturnType is not a subtype");
	        }
		return arg;
	}

	@Override
	public Void methodCall(MethodCall ast, Void arg) {
		etc.visit(ast.getMethodCallExpr(), localSymbolTable);
		return arg;
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
		Symbol.TypeSymbol conditionType = etc.visit(ast.condition(), localSymbolTable);

        if (!conditionType.equals(Symbol.PrimitiveTypeSymbol.booleanType)) {
            throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "while requires condition to be of type boolean");
        }
		return visit(ast.body(),arg);
	}
	
	

}
