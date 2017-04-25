package cd.frontend.semantic;

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

public class ExprTypeChecker extends ExprVisitor<Symbol.TypeSymbol, SymbolTable>{	
	
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
        
        switch (ast.operator){
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
        		
        		if (!leftType.equals(PrimitiveTypeSymbol.booleanType) || !rightType.equals(PrimitiveTypeSymbol.booleanType)) {
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
        		
        		if (!leftType.equals(PrimitiveTypeSymbol.intType) ||!rightType.equals(PrimitiveTypeSymbol.intType)) {
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
		// TODO Auto-generated method stub
		return super.cast(ast, arg);
	}

	@Override
	public TypeSymbol field(Field ast, SymbolTable arg) {
		System.out.println("==ExprCheck - Field");
		// TODO Auto-generated method stub
		return super.field(ast, arg);
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
		
		Var caller = (Var) ast.allArguments().get(0);
		
		String callerClass = arg.get(caller.name).name;
		String calleeMethod = ast.methodName;
		
		if(!TypeChecker.classTable.get(callerClass).containsKey(calleeMethod))
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_METHOD);
		
		System.out.println(TypeChecker.classTable.get(callerClass).get(calleeMethod)); 

		return super.methodCall(ast, arg);
	}

	@Override
	public TypeSymbol newObject(NewObject ast, SymbolTable arg) {
		System.out.println("==ExprCheck - NewObject");
		
//		if(!arg.containsKey(ast.typeName)) //should be checked in the filler
//            throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE);
		return (TypeSymbol) TypeChecker.symbolTable.get(ast.typeName);
	}

	@Override
	public TypeSymbol newArray(NewArray ast, SymbolTable arg) {
		System.out.println("==ExprCheck - NewArray");
		Symbol.TypeSymbol type = visit(ast.arg(), arg);
		
//		if (!type.equals(PrimitiveTypeSymbol.intType)) {
//            throw new SemanticFailure(SemanticFailure.Cause.TYPE_ERROR);
//        }
		
		Symbol.TypeSymbol typeSym = (Symbol.TypeSymbol) arg.get(ast.typeName);
		
		if (typeSym == null) {
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_TYPE);
        }
	
		return typeSym;
	}

	@Override
	public TypeSymbol nullConst(NullConst ast, SymbolTable arg) {
		System.out.println("==ExprCheck - NullConst");
		ast.type = Symbol.ClassSymbol.nullType;
		return ast.type;
	}

	@Override
	public TypeSymbol thisRef(ThisRef ast, SymbolTable arg) {
		System.out.println("==ExprCheck - ThisRef");
		
		return arg.get("This");
	}

	@Override
	public TypeSymbol unaryOp(UnaryOp ast, SymbolTable arg) {
		System.out.println("==ExprCheck - UnaryOp");
		Symbol.TypeSymbol type = visit(ast.arg(), arg);;
		
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
		//System.out.println(ast.type.name)
		
		if(!arg.containsKey(ast.name))
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_VARIABLE, "No Variable " + ast.name + " was found");
		
		return (TypeSymbol) arg.get(ast.name);
	}
	
	

}
