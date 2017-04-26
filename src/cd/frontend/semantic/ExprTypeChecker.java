package cd.frontend.semantic;

import cd.ir.Ast;
import cd.ir.Ast.BinaryOp;
import cd.ir.Ast.BooleanConst;
import cd.ir.Ast.BuiltInRead;
import cd.ir.Ast.Cast;
import cd.ir.Ast.Expr;
import cd.ir.Ast.Field;
import cd.ir.Ast.Index;
import cd.ir.Ast.IntConst;
import cd.ir.Ast.MethodCallExpr;
import cd.ir.Ast.NewArray;
import cd.ir.Ast.NewObject;
import cd.ir.Ast.NullConst;
import cd.ir.Ast.ThisRef;
import cd.ir.Ast.UnaryOp;
import cd.ir.Ast.Var;
import cd.ir.ExprVisitor;
import cd.ir.Symbol;
import cd.ir.Symbol.PrimitiveTypeSymbol;
import cd.ir.Symbol.TypeSymbol;

public class ExprTypeChecker extends ExprVisitor<Symbol.TypeSymbol, SymbolTable> {

	@Override
	public TypeSymbol visit(Expr ast, SymbolTable arg) {
		// TODO Auto-generated method stub
		return super.visit(ast, arg);
	}

	@Override
	public TypeSymbol visitChildren(Expr ast, SymbolTable arg) {
		// TODO Auto-generated method stub
		return super.visitChildren(ast, arg);
	}

	@Override
	public TypeSymbol binaryOp(BinaryOp ast, SymbolTable arg) {
		System.out.println("==ExprCheck - BinaryOP");

		Symbol.TypeSymbol leftType = visit(ast.left(), arg);
		Symbol.TypeSymbol rightType = visit(ast.right(), arg);

		switch (ast.operator) {
		case B_TIMES:
		case B_DIV:
		case B_MOD:
		case B_PLUS:
		case B_MINUS:

			if (!leftType.equals(PrimitiveTypeSymbol.intType) || !rightType.equals(PrimitiveTypeSymbol.intType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}

			return Symbol.PrimitiveTypeSymbol.intType;

		case B_AND:
		case B_OR:

			if (!leftType.equals(PrimitiveTypeSymbol.booleanType)
					|| !rightType.equals(PrimitiveTypeSymbol.booleanType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}

			return Symbol.PrimitiveTypeSymbol.booleanType;

		case B_EQUAL:
		case B_NOT_EQUAL:

			if (!leftType.isSubType(rightType) && !rightType.isSubType(leftType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}

			return Symbol.PrimitiveTypeSymbol.booleanType;

		case B_LESS_THAN:
		case B_LESS_OR_EQUAL:
		case B_GREATER_THAN:
		case B_GREATER_OR_EQUAL:

			if (!leftType.equals(PrimitiveTypeSymbol.intType) || !rightType.equals(PrimitiveTypeSymbol.intType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}

			return Symbol.PrimitiveTypeSymbol.booleanType;
		default:
			throw new RuntimeException("Unsupported operator");

		}

	}

	@Override
	public TypeSymbol booleanConst(BooleanConst ast, SymbolTable arg) {
		System.out.println("==ExprCheck - BooleanConst");
		ast.type = Symbol.PrimitiveTypeSymbol.booleanType;
		return ast.type;
	}

	@Override
	public TypeSymbol builtInRead(BuiltInRead ast, SymbolTable arg) {
		System.out.println("==ExprCheck - BuiltInRead");
		ast.type = Symbol.PrimitiveTypeSymbol.intType;
		return ast.type;
	}

	@Override
	public TypeSymbol cast(Cast ast, SymbolTable arg) {
		System.out.println("==ExprCheck - Cast");

		System.out.println(ast.arg());
		System.out.println(ast.typeName);

		// TODO check if cast is poss

		arg.print();
		TypeSymbol tr = visit(ast.arg(), arg); // type right side
		TypeSymbol tl = TypeChecker.symbolTable.get(ast.typeName);

		if (tl == null)
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE);

		if (!tr.equals(tl)) {
			System.out.println("here1");
			
			System.out.println(TypeChecker.classTable.get(tl.name).extendsFrom);
			System.out.println(tr.name);
			System.out.println(TypeChecker.classTable.get(tr.name).extendsFrom);
			
			if(TypeChecker.classTable.get(tl.name).extendsFrom.contains(tr.name) || TypeChecker.classTable.get(tr.name).extendsFrom.contains(tl.name)){
				//OK
			}else{
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}
			
				
//			String extendL = TypeChecker.classTable.get(tl.name).extendsFromm;
//			String extendR = TypeChecker.classTable.get(tr.name).extendsFromm;
//			if (extendL == null	&& extendR == null) { // no																			// extend
//				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
//			}
//			else if(extendL != null && extendR == null && !TypeChecker.symbolTable.get(extendL).equals(tr)){
//				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
//			}
//			else if(extendR != null && extendL == null && !TypeChecker.symbolTable.get(extendR).equals(tl)){
//				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
//			}
			
			
			
//			else if (TypeChecker.classTable.get(tl.name).extendsFrom != null
//					&& !TypeChecker.symbolTable.get(TypeChecker.classTable.get(tl.name).extendsFrom).equals(tr)) { // wrong
//																													// extend
//				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
//			} else if (TypeChecker.classTable.get(tr.name).extendsFrom != null
//					&& !TypeChecker.symbolTable.get(TypeChecker.classTable.get(tr.name).extendsFrom).equals(tl)) { // wrong
//																													// extend
//				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
//			}
		}
		return TypeChecker.symbolTable.get(ast.typeName);

	}

	@Override
	public TypeSymbol field(Field ast, SymbolTable arg) {
		System.out.println("==ExprCheck - Field");

		String fieldName = ast.fieldName;
		TypeSymbol classN = visit(ast.arg(), arg);

		System.out.println("fN:" + fieldName);

		System.out.println("classN:" + classN.name);

		if (!TypeChecker.classTable.containsKey(classN.name))
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);

		if (TypeChecker.classTable.get(classN.name).getFieldType(fieldName) == null)
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_FIELD);

		return TypeChecker.classTable.get(classN.name).getFieldType(fieldName);

	}

	@Override
	public TypeSymbol index(Index ast, SymbolTable arg) {
		System.out.println("==ExprCheck - Index");

		Symbol.TypeSymbol leftType = visit(ast.left(), arg);
		Symbol.TypeSymbol rightType = visit(ast.right(), arg);

		if (!rightType.equals(Symbol.PrimitiveTypeSymbol.intType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "Mismatch");
		}
		if (!leftType.isArrayType()) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR, "It is not an array");
		}

		ast.type = ((Symbol.ArrayTypeSymbol) leftType).elementType;
		return ast.type;
	}

	@Override
	public TypeSymbol intConst(IntConst ast, SymbolTable arg) {
		System.out.println("==ExprCheck - IntConst");
		return Symbol.PrimitiveTypeSymbol.intType;
	}

	@Override
	public TypeSymbol methodCall(MethodCallExpr ast, SymbolTable arg) {
		System.out.println("==ExprCheck - MethodCall");

		arg.print();
		String callerClass;

		if (ast.allArguments().get(0) instanceof Ast.ThisRef) {
			callerClass = arg.inClass;
		}else if(ast.allArguments().get(0) instanceof Ast.MethodCallExpr){
			MethodCallExpr e = (MethodCallExpr) ast.allArguments().get(0);
			System.out.println(e.methodName);		
			//TODO check if exist
			callerClass = arg.inClass;
			
		}else if(ast.allArguments().get(0) instanceof Ast.Field){
			Field f = (Field) ast.allArguments().get(0);
			System.out.println(f.fieldName);		
			//TODO check if exist
			callerClass = arg.inClass;
			
		} else {
			
			
			System.out.println(ast.allArguments());
			
			Var caller = (Var) ast.allArguments().get(0);

			// if(TypeChecker.methodTable.get(currentClass+currentMethod.name)
			// == null)
			// throw new
			// SemanticFailure(SemanticFailure.Cause.NO_SUCH_VARIABLE); //TODO
			// //Error correct?
			//
			// if(TypeChecker.methodTable.get(currentClass+currentMethod.name).get(caller.name)
			// == null)
			// throw new
			// SemanticFailure(SemanticFailure.Cause.NO_SUCH_VARIABLE);

			callerClass = arg.getFieldType(caller.name).name;
		}
		String calleeMethod = ast.methodName;

		if (TypeChecker.classTable.get(callerClass) == null)
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);

		if (!TypeChecker.classTable.get(callerClass).containsFunction(calleeMethod))
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_METHOD);

		if (TypeChecker.methodTable.get(callerClass + calleeMethod).parameterNames.size() != ast
				.argumentsWithoutReceiver().size())
			throw new SemanticFailure(SemanticFailure.Cause.WRONG_NUMBER_OF_ARGUMENTS);

		System.out.println("Argument Check:");
		for (int i = 0; i < ast.argumentsWithoutReceiver().size(); i++) {

			Expr argument = ast.argumentsWithoutReceiver().get(i);
			Symbol.TypeSymbol argumentType = visit(argument, arg);

			String argName = TypeChecker.methodTable.get(callerClass + calleeMethod).parameterNames.get(i).toString();

			System.out.println(TypeChecker.methodTable.get(callerClass + calleeMethod).getFieldType(argName).name);
			System.out.println(argumentType.name);

			TypeSymbol typCal = TypeChecker.methodTable.get(callerClass + calleeMethod).getFieldType(argName);

			if (!typCal.equals(argumentType)) {
				
				System.out.println(TypeChecker.classTable.get(typCal.name));
				
				if (TypeChecker.classTable.get(argumentType.name).extendsFrom != null
						&& TypeChecker.classTable.get(argumentType.name).extendsFrom.contains(typCal.name)) {
				} else {
					throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
				}

			}

		}

		System.out.println(TypeChecker.classTable.get(callerClass).getFunctionType(calleeMethod));

		return TypeChecker.classTable.get(callerClass).getFunctionType(calleeMethod);

	}

	@Override
	public TypeSymbol newObject(NewObject ast, SymbolTable arg) {
		System.out.println("==ExprCheck - NewObject");

		// if(!arg.containsKey(ast.typeName)) //should be checked in the filler
		// throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE);
		return (TypeSymbol) TypeChecker.symbolTable.get(ast.typeName);
	}

	@Override
	public TypeSymbol newArray(NewArray ast, SymbolTable arg) {
		System.out.println("==ExprCheck - NewArray");
		Symbol.TypeSymbol type = visit(ast.arg(), arg);

		if (!type.equals(PrimitiveTypeSymbol.intType)) {
			throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
		}

		// Symbol.TypeSymbol typeSym = (Symbol.TypeSymbol)
		// arg.get(ast.typeName);

		Symbol.TypeSymbol typeSym = (Symbol.TypeSymbol) TypeChecker.symbolTable.get(ast.typeName);

		if (typeSym == null) {
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE);
		}

		return typeSym;
	}

	@Override
	public TypeSymbol nullConst(NullConst ast, SymbolTable arg) {
		System.out.println("==ExprCheck - NullConst");
		return Symbol.ClassSymbol.nullType;
	}

	@Override
	public TypeSymbol thisRef(ThisRef ast, SymbolTable arg) {
		System.out.println("==ExprCheck - ThisRef");
		return TypeChecker.symbolTable.get(arg.inClass);
	}

	@Override
	public TypeSymbol unaryOp(UnaryOp ast, SymbolTable arg) {
		System.out.println("==ExprCheck - UnaryOp");
		Symbol.TypeSymbol type = visit(ast.arg(), arg);
		;

		switch (ast.operator) {
		case U_PLUS:
		case U_MINUS:

			if (!type.equals(PrimitiveTypeSymbol.intType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}

			return PrimitiveTypeSymbol.intType;

		case U_BOOL_NOT:

			if (!type.equals(PrimitiveTypeSymbol.booleanType)) {
				throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
			}
			return PrimitiveTypeSymbol.booleanType;

		default:
			throw new RuntimeException("Unsupported operator");
		}

	}

	@Override
	public TypeSymbol var(Var ast, SymbolTable arg) {
		System.out.println("==ExprCheck - Variable");

		// TODO Auto-generated method stub

		if (!arg.containsField(ast.name)) {
			System.out.println("Failure " + ast.name);
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_VARIABLE, "No Variable " + ast.name + " was found");
		}
		return arg.getFieldType(ast.name);
	}

}
