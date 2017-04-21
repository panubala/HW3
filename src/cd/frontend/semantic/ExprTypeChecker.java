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
		// TODO Auto-generated method stub
		return super.binaryOp(ast, arg);
	}

	@Override
	public TypeSymbol booleanConst(BooleanConst ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.booleanConst(ast, arg);
	}

	@Override
	public TypeSymbol builtInRead(BuiltInRead ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.builtInRead(ast, arg);
	}

	@Override
	public TypeSymbol cast(Cast ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.cast(ast, arg);
	}

	@Override
	public TypeSymbol field(Field ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.field(ast, arg);
	}

	@Override
	public TypeSymbol index(Index ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.index(ast, arg);
	}

	@Override
	public TypeSymbol intConst(IntConst ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.intConst(ast, arg);
	}

	@Override
	public TypeSymbol methodCall(MethodCallExpr ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.methodCall(ast, arg);
	}

	@Override
	public TypeSymbol newObject(NewObject ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.newObject(ast, arg);
	}

	@Override
	public TypeSymbol newArray(NewArray ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.newArray(ast, arg);
	}

	@Override
	public TypeSymbol nullConst(NullConst ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.nullConst(ast, arg);
	}

	@Override
	public TypeSymbol thisRef(ThisRef ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.thisRef(ast, arg);
	}

	@Override
	public TypeSymbol unaryOp(UnaryOp ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.unaryOp(ast, arg);
	}

	@Override
	public TypeSymbol var(Var ast, SymbolTable<VariableSymbol> arg) {
		// TODO Auto-generated method stub
		return super.var(ast, arg);
	}

}
