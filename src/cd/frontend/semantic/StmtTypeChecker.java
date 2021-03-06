package cd.frontend.semantic;

import java.util.Map;

import cd.ir.Ast;
import cd.ir.Ast.Assign;
import cd.ir.Ast.BuiltInWrite;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.Expr;
import cd.ir.Ast.IfElse;
import cd.ir.Ast.MethodCall;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.ReturnStmt;
import cd.ir.Ast.Var;
import cd.ir.Ast.WhileLoop;
import cd.ir.AstVisitor;
import cd.ir.Symbol;
import cd.ir.Symbol.MethodSymbol;
import cd.ir.Symbol.PrimitiveTypeSymbol;

public class StmtTypeChecker extends AstVisitor<Void, Void> {
	private ExprTypeChecker exprChecker;
	private MethodSymbol currentMethod;
	private String currentClass;

	public StmtTypeChecker(Symbol.ClassSymbol classSymbol) {
		this.exprChecker = new ExprTypeChecker();
	}

	@Override
	public Void assign(Assign ast, Void arg) {
		System.out.println("==StmtCheck - Assign");

		if (!(ast.left() instanceof Ast.Var) && !(ast.left() instanceof Ast.Field)
				&& !(ast.left() instanceof Ast.Index))
			throw new SemanticFailure(SemanticFailure.Cause.NOT_ASSIGNABLE);

		Symbol.TypeSymbol leftType = exprChecker.visit(ast.left(),
				TypeChecker.methodTable.get(currentClass + currentMethod.name));
		Symbol.TypeSymbol rightType = exprChecker.visit(ast.right(),
				TypeChecker.methodTable.get(currentClass + currentMethod.name));

		System.out.println("Left Type: " + leftType.name + ", Right Type: " + rightType.name);

		if (!rightType.isSubType(leftType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "Assignment must have compatible types.");
		}

		return arg;
	}

	@Override
	public Void builtInWrite(BuiltInWrite ast, Void arg) {
		System.out.println("==StmtCheck - Write");

		Symbol.TypeSymbol type = exprChecker.visit(ast.arg(),
				TypeChecker.methodTable.get(currentClass + currentMethod.name));

		if (!type.equals(PrimitiveTypeSymbol.intType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
		}
		return arg;
	}

	@Override
	public Void classDecl(ClassDecl ast, Void arg) {
		System.out.println("==StmtCheck - classDecl");
		currentClass = ast.name;

		return super.classDecl(ast, arg);
	}

	@Override
	public Void methodDecl(MethodDecl ast, Void arg) {
		System.out.println("==StmtCheck - MethodDecl");

		currentMethod = ast.sym;

		visit(ast.decls(), arg);
		Void result = visit(ast.body(), null);

		// check return Stmt
		if (!ast.returnType.equals("void")) {
			boolean rtnStmt = false;
			for (Ast stmt : ast.body().children()) {
				if (stmt instanceof Ast.ReturnStmt) {
					rtnStmt = true;
					break;
				}
			}

			if (!rtnStmt) {
				throw new SemanticFailure(SemanticFailure.Cause.MISSING_RETURN);
			}
		}
		return result;
	}

	@Override
	public Void ifElse(IfElse ast, Void arg) {
		System.out.println("==StmtCheck - IfElse");

		Symbol.TypeSymbol conditionType = exprChecker.visit(ast.condition(),
				TypeChecker.methodTable.get(currentClass + currentMethod.name));

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

		// TypeChecker.methodTable.get(currentClass +
		// currentMethod.name).hasReturnStmt=true;

		if (ast.arg() == null) {
			if (!currentMethod.returnType.equals(PrimitiveTypeSymbol.voidType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}
		} else {
			ast.arg().type = exprChecker.visit(ast.arg(),
					TypeChecker.methodTable.get(currentClass + currentMethod.name));

			if (!ast.arg().type.isSubType(currentMethod.returnType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "ReturnType is not a subtype");
			}
		}

		return arg;
	}

	@Override
	public Void methodCall(MethodCall ast, Void arg) {
		System.out.println("==StmtCheck - MethodCall");
		String callerClass;
		if (ast.getMethodCallExpr().allArguments().get(0) instanceof Ast.ThisRef) {
			callerClass = currentClass;
		} else {
			Var caller = (Var) ast.getMethodCallExpr().allArguments().get(0);
			if (TypeChecker.methodTable.get(currentClass + currentMethod.name) == null)
				throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_VARIABLE);
			if (TypeChecker.methodTable.get(currentClass + currentMethod.name).getFieldType(caller.name) == null)
				throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_VARIABLE);

			callerClass = TypeChecker.methodTable.get(currentClass + currentMethod.name).getFieldType(caller.name).name;
		}
		String calleeMethod = ast.getMethodCallExpr().methodName;

		System.out.println(callerClass);

		if (TypeChecker.classTable.get(callerClass) == null)
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);

		System.out.println(calleeMethod);
		if (!TypeChecker.classTable.get(callerClass).containsFunction(calleeMethod))
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_METHOD);

		if (TypeChecker.methodTable.get(callerClass + calleeMethod).parameterNames.size() != ast.getMethodCallExpr()
				.argumentsWithoutReceiver().size())
			throw new SemanticFailure(SemanticFailure.Cause.WRONG_NUMBER_OF_ARGUMENTS);

		System.out.println("Argument Check:");
		for (int i = 0; i < ast.getMethodCallExpr().argumentsWithoutReceiver().size(); i++) {

			Expr argument = ast.getMethodCallExpr().argumentsWithoutReceiver().get(i);

			Symbol.TypeSymbol argumentType = exprChecker.visit(argument,
					TypeChecker.methodTable.get(currentClass + currentMethod.name));

			String argName = TypeChecker.methodTable.get(callerClass + calleeMethod).parameterNames.get(i).toString();

			if (!TypeChecker.methodTable.get(callerClass + calleeMethod).getFieldType(argName).equals(argumentType))
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);

		}

		// exprChecker.visit(ast.getMethodCallExpr(),
		// TypeChecker.methodTable.get(callerClass+calleeMethod));
		return arg;
	}

	@Override
	public Void whileLoop(WhileLoop ast, Void arg) {
		System.out.println("==StmtCheck - WhileLoop");
		Symbol.TypeSymbol conditionType = exprChecker.visit(ast.condition(),
				TypeChecker.methodTable.get(currentClass + currentMethod.name));

		if (!conditionType.equals(Symbol.PrimitiveTypeSymbol.booleanType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR,
					"while requires condition to be of type boolean");
		}
		return visit(ast.body(), arg);
	}

}
