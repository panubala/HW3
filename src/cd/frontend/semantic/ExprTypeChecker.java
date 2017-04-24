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
import cd.ir.Symbol.TypeSymbol;
import cd.ir.Symbol.VariableSymbol;

public class ExprTypeChecker extends ExprVisitor<Symbol.TypeSymbol, SymbolTable<Symbol.VariableSymbol>>{	
	
	@Override
	public TypeSymbol visit(Expr ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.visit(ast, arg);
	}

	@Override
	public TypeSymbol visitChildren(Expr ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.visitChildren(ast, arg);
	}

	@Override
	public TypeSymbol binaryOp(BinaryOp ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - BinaryOP");
		// TODO Auto-generated method stub
		return super.binaryOp(ast, arg);
	}

	@Override
	public TypeSymbol booleanConst(BooleanConst ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - BooleanConst");
		ast.type = Symbol.PrimitiveTypeSymbol.booleanType;
		return ast.type;
	}

	@Override
	public TypeSymbol builtInRead(BuiltInRead ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - BuiltInRead");
		ast.type = Symbol.PrimitiveTypeSymbol.intType;
		return ast.type;
	}

	@Override
	public TypeSymbol cast(Cast ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - Cast");
		// TODO Auto-generated method stub
		return super.cast(ast, arg);
	}

	@Override
	public TypeSymbol field(Field ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - Field");
		// TODO Auto-generated method stub
		return super.field(ast, arg);
	}

	@Override
	public TypeSymbol index(Index ast, SymbolTable<VariableSymbol> arg) {
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
	public TypeSymbol intConst(IntConst ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - IntConst");
		ast.type = Symbol.PrimitiveTypeSymbol.intType;
		return ast.type;
	}

	@Override
	public TypeSymbol methodCall(MethodCallExpr ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - MethodCall");
		// TODO Auto-generated method stub
		return super.methodCall(ast, arg);
	}

	@Override
	public TypeSymbol newObject(NewObject ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - NewObject");
		// TODO Auto-generated method stub
		return super.newObject(ast, arg);
	}

	@Override
	public TypeSymbol newArray(NewArray ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - NewArray");
		// TODO Auto-generated method stub
		return super.newArray(ast, arg);
	}

	@Override
	public TypeSymbol nullConst(NullConst ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - NullConst");
		ast.type = Symbol.ClassSymbol.nullType;
		return ast.type;
	}

	@Override
	public TypeSymbol thisRef(ThisRef ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - ThisRef");
		ast.type = ((Symbol.VariableSymbol) arg.get("this")).type;
		return ast.type;
	}

	@Override
	public TypeSymbol unaryOp(UnaryOp ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - UnaryOp");
		// TODO Auto-generated method stub
		return super.unaryOp(ast, arg);
	}

	@Override
	public TypeSymbol var(Var ast, SymbolTable<VariableSymbol> arg) {
		System.out.println("==ExprCheck - Variable");
		// TODO Auto-generated method stub
		//System.out.println(ast.type.name);
		System.out.println(arg.get(ast.name));
		if(!arg.containsKey(ast.name))
			throw new SemanticFailure(SemanticFailure.Cause.NO_SUCH_VARIABLE, "No Variable " + ast.name + " was found");
		return (TypeSymbol) arg.get(ast.name);
	}

}
