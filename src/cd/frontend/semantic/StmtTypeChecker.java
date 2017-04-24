package cd.frontend.semantic;

import java.util.HashMap;
import java.util.List;
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
	// private SymbolTable globalSymboltable;
	private ExprTypeChecker exprChecker;
	private String currentMethod;
	private String currentClass;

	private SymbolTable scopeSymbolTable;

	public StmtTypeChecker(Symbol.ClassSymbol classSymbol) {
		// TODO Auto-generated constructor stub
		this.exprChecker = new ExprTypeChecker();
	}

	@Override
	public Void visit(Ast ast, Void arg) {
		// TOD Auto-generated method stub
		return super.visit(ast, arg);
	}

	@Override
	public Void visitChildren(Ast ast, Void arg) {
		// TODO Auto-generated method stub
		return super.visitChildren(ast, arg);
	}

	@Override
	public Void assign(Assign ast, Void arg) {
		System.out.println("==StmtCheck - Assign");
		Symbol.TypeSymbol leftType = exprChecker.visit(ast.left(), TypeChecker.methodTable.get(currentClass + currentMethod));
		Symbol.TypeSymbol rightType = exprChecker.visit(ast.right(), TypeChecker.methodTable.get(currentClass + currentMethod));

		System.out.println("Left Type: " + leftType.name + ", Right Type: " + rightType.name);

		if (!rightType.isSubType(leftType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "Assignment must have compatible types.");
		}

		return arg;
	}

	@Override
	public Void builtInWrite(BuiltInWrite ast, Void arg) {
		System.out.println("==StmtCheck - Write");
		// TODO Auto-generated method stub
		return super.builtInWrite(ast, arg);
	}

	@Override
	public Void builtInWriteln(BuiltInWriteln ast, Void arg) {
		System.out.println("==StmtCheck - Writeln");
		// TODO Auto-generated method stub
		return super.builtInWriteln(ast, arg);
	}

	@Override
	public Void classDecl(ClassDecl ast, Void arg) {
		System.out.println("==StmtCheck - classDecl");
		// TODO Auto-generated method stub
		currentClass = ast.name;

		return super.classDecl(ast, arg);
	}

	@Override
	public Void methodDecl(MethodDecl ast, Void arg) {
		System.out.println("==StmtCheck - MethodDecl");

		currentMethod = ast.name;
		// localSymbolTable = new SymbolTable<>();

		visit(ast.decls(), null); //TODO ?
		Void result = visit(ast.body(), null);

		currentMethod = null;

		return result;
	}

	@Override
	public Void varDecl(VarDecl ast, Void arg) {
		System.out.println("==StmtCheck - VarDecl");
		// TODO Auto-generated method stub

		return super.varDecl(ast, arg);
	}

	@Override
	public Void ifElse(IfElse ast, Void arg) {
		System.out.println("==StmtCheck - IfElse");

		Symbol.TypeSymbol conditionType = exprChecker.visit(ast.condition(), TypeChecker.symbolTable);

		if (!conditionType.equals(Symbol.PrimitiveTypeSymbol.booleanType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR,
					"ifelse requires condition to be of type boolean");
		}

		visit(ast.then(), arg);

		return visit(ast.otherwise(), arg);
	}

	@Override
	public Void returnStmt(ReturnStmt ast, Void arg) {
		System.out.println("==StmtCheck - Return");
		ast.arg().type = exprChecker.visit(ast.arg(), TypeChecker.symbolTable);
//		if (!ast.arg().type.isSubType(currentMethod.returnType)) {
//			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "ReturnType is not a subtype");
//		}
		return arg;
	}

	@Override
	public Void methodCall(MethodCall ast, Void arg) {
		System.out.println("==StmtCheck - MethodCall");
		exprChecker.visit(ast.getMethodCallExpr(), TypeChecker.symbolTable);
		return arg;
	}

	@Override
	public Void nop(Nop ast, Void arg) {
		System.out.println("==StmtCheck - nop");
		// TODO Auto-generated method stub
		return super.nop(ast, arg);
	}

	@Override
	public Void seq(Seq ast, Void arg) {
		System.out.println("==StmtCheck - Seq");
		// TODO Auto-generated method stub
		return super.seq(ast, arg);
	}

	@Override
	public Void whileLoop(WhileLoop ast, Void arg) {
		System.out.println("==StmtCheck - WhileLoop");
		Symbol.TypeSymbol conditionType = exprChecker.visit(ast.condition(), TypeChecker.symbolTable);

		if (!conditionType.equals(Symbol.PrimitiveTypeSymbol.booleanType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR,
					"while requires condition to be of type boolean");
		}
		return visit(ast.body(), arg);
	}

}
