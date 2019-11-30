/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ast.*;
import lexer.Lexer;
import lexer.Token;

public class Compiler {

	public Compiler() { }

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectAnnotation> metaobjectCallList = new ArrayList<>();
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();;
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		boolean thereWasAnError = false;
		while (lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open")) ||
				lexer.token == Token.ANNOT) {
			try {
				while (lexer.token == Token.ANNOT) {
					metaobjectAnnotation(metaobjectCallList);
				}
				ClassDec classDec = classDec();
				CianetoClassList.add(new TypeCianetoClass(classDec.getName()));
			}
			catch(CompilerError e) {
				// if there was an exception, there is a compilation error
				thereWasAnError = true;
			}
			catch (RuntimeException e) {
				e.printStackTrace();
				thereWasAnError = true;
			}

		}
		if ( !thereWasAnError && lexer.token != Token.EOF ) {
			try {
				error("End of file expected");
			}
			catch( CompilerError e) {
			}
		}
		return program;
	}

	/**  parses a metaobject annotation as <code>{@literal @}cep(...)</code> in <br>
     * <code>
     * {@literal @}cep(5, "'class' expected") <br>
     * class Program <br>
     *     func run { } <br>
     * end <br>
     * </code>
     *

	 */
	@SuppressWarnings("incomplete-switch")
	private void metaobjectAnnotation(ArrayList<MetaobjectAnnotation> metaobjectAnnotationList) {
		String name = lexer.getMetaobjectName();
		int lineNumber = lexer.getLineNumber();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		boolean getNextToken = false;
		if ( lexer.token == Token.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Token.LITERALINT || lexer.token == Token.LITERALSTRING ||
					lexer.token == Token.ID ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case ID:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Token.COMMA )
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
			else {
				getNextToken = true;
			}
		}
		switch ( name ) {
		case "nce":
			if ( metaobjectParamList.size() != 0 )
				error("Annotation 'nce' does not take parameters");
			break;
		case "cep":
			int sizeParamList = metaobjectParamList.size();
			if ( sizeParamList < 2 || sizeParamList > 4)
				error("Annotation 'cep' takes two, three, or four parameters");

			if ( !( metaobjectParamList.get(0) instanceof Integer)  ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String))
				error("The second parameter of annotation 'cep' should be a literal string");
			if ( sizeParamList >= 3 && !( metaobjectParamList.get(2) instanceof String))
				error("The third parameter of annotation 'cep' should be a literal string");
			if ( sizeParamList >= 4 && !( metaobjectParamList.get(3) instanceof String))
				error("The fourth parameter of annotation 'cep' should be a literal string");
			break;
		case "annot":
			if ( metaobjectParamList.size() < 2  ) {
				error("Annotation 'annot' takes at least two parameters");
			}
			for ( Object p : metaobjectParamList ) {
				if ( !(p instanceof String) ) {
					error("Annotation 'annot' takes only String parameters");
				}
			}
			if ( ! ((String ) metaobjectParamList.get(0)).equalsIgnoreCase("check") )  {
				error("Annotation 'annot' should have \"check\" as its first parameter");
			}
			break;
		default:
			error("Annotation '" + name + "' is illegal");
		}
		metaobjectAnnotationList.add(new MetaobjectAnnotation(name, metaobjectParamList));
		if ( getNextToken ) lexer.nextToken();
	}

	private ClassDec classDec() {
		String superclassName = "";
		ClassDec superclass = null;
		List<Member> membersList = null;
		
		if (lexer.token == Token.ID && lexer.getStringValue().equals("open")) {
			lexer.nextToken();
		}
		
		if (lexer.token != Token.CLASS)
			error("'class' expected");
		lexer.nextToken();
		
		if ( lexer.token != Token.ID )
			error("Identifier expected");
		String className = lexer.getStringValue();
		lexer.nextToken();
		
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID )
				error("Identifier expected");
			superclassName = lexer.getStringValue();
			
			superclass = (ClassDec) symbolTable.getInGlobal(superclassName);
			
			if (superclass == null) {
				error("Parent class '" + superclassName + "' not found");
			}

			lexer.nextToken();
		}
		
		ClassDec classDec = new ClassDec(className, superclass);
		symbolTable.putInGlobal(className, classDec);
		this.currentClass = classDec;

		membersList = memberList();
		
		classDec.setMemberList(membersList);
		
		if ( lexer.token != Token.END)
			error("'end' expected");
		lexer.nextToken();
		
		if (classDec.getName().equals("Program")) {
			if (!hasMethodRun(classDec)) {
				error("Class 'Program' must have a 'run' method");
			}
		}
		
		symbolTable.clearClass();
		
		return classDec;

	}
	
	private Boolean hasMethodRun(ClassDec classDec) {
		Boolean hasMethodRun = false;
		List<Member> classMembers = classDec.getMemberList();
		
		for (Member m : classMembers) {
			if (m instanceof MethodDec && ((MethodDec) m).getName().equals("run")) {
				hasMethodRun = true;
				break;
			}
		}
		
		return hasMethodRun;
	}

	private List<Member> memberList() {
		List<Member> memberList = new ArrayList<>();
		Qualifier qualifier = null;
		Member member = null;
		
		while (true) {
			qualifier = qualifier();
			if (lexer.token == Token.VAR) {
				member = fieldDec(qualifier);
				memberList.add(member);
				
			}
			else if (lexer.token == Token.FUNC) {
				member = methodDec(qualifier);
				memberList.add(member);
			}
			else {
				break;
			}
		}
		
		return memberList;
	}

	private void error(String msg) {
		this.signalError.showError(msg);
	}


	private void next() {
		lexer.nextToken();
	}

	private void check(Token shouldBe, String msg) {
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
	}

	private MethodDec methodDec(Qualifier qualifier) {
		lexer.nextToken();
		
		if (lexer.token != Token.ID && lexer.token != Token.IDCOLON) {
			error("An identifier or identifer: was expected after 'func'");
		}
		
		String methodName = lexer.getStringValue();
		List<Variable> formalParamDec = null;
		Type returnType = null;
		
		if (currentClass.findMethodInSuper(methodName) != null
				&& qualifier != null && !qualifier.getQualifiers().contains(Token.OVERRIDE)) {
			error("Overriden method '" + methodName + "' should be preceded by 'override'");
		}
		
		if (symbolTable.getInClass(methodName) != null) {
			error("Method '" + methodName + "' already declared in this scope");
		}
		
		if (lexer.token == Token.IDCOLON) {
			// keyword method. It has parameters
			lexer.nextToken();
			formalParamDec = formalParamDec();
		} else if (lexer.token == Token.ID) {
			// unary method
			lexer.nextToken();
		}
		
		if (currentClass.getName().equals("Program")
				&& methodName.equals("run:")
				&& formalParamDec != null) {
			error("Method 'run:' of class 'Program' cannot take parameters");
		}
		
		if (lexer.token == Token.MINUS_GT) {
			// method declared a return type
			lexer.nextToken();
			returnType = type();
		}
		
		if (lexer.token != Token.LEFTCURBRACKET) {
			error("'{' expected");
		}
		next();
		
		MethodDec method;
		
		if (formalParamDec != null) {
			method = new MethodDec(qualifier, methodName, formalParamDec, returnType);
		} else {
			method = new MethodDec(qualifier, methodName, returnType);
		}
		
		symbolTable.putInClass(methodName, method);
		this.currentMethod = method;
		
		List<Statement> stmtList = statementList();
		
		method.setStmtList(stmtList);
		
		if (lexer.token != Token.RIGHTCURBRACKET) {
			error("'}' expected");
		}
		next();
		
		symbolTable.clearLocal();
		
		return method;
	}

	private List<Statement> statementList() {
		List<Statement> statementList = new ArrayList<>();
		
		while (lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.UNTIL) {
			statementList.add(statement());
		}
		
		return statementList;
	}

	private Statement statement() {
		Statement statement = null;
		boolean checkSemiColon = true;
		
		switch (lexer.token) {
		case IF:
			statement = ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			statement = whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			statement = returnStat();
			break;
		case BREAK:
			statement = breakStat();
			break;
		case SEMICOLON:
			next();
			checkSemiColon = false;
			break;
		case REPEAT:
			statement = repeatStat();
			checkSemiColon = false;
			break;
		case VAR:
			statement = localDec();
			break;
		case ASSERT:
			statement = assertStat();
			break;
		default:
			if (lexer.token == Token.ID && lexer.getStringValue().equals("Out")) {
				statement = printStat();
			}
			else {
				statement = new AssignStat(assignExpr());
			}

		}
		if (checkSemiColon) {
			check(Token.SEMICOLON, "';' expected");
		}
		
		return statement;
	}

	private LocalDec localDec() {
		Expr expr = null;
		
		next();
		
		Type type = type();
		
		check(Token.ID, "A variable name was expected");
		
		List<Variable> variables = new ArrayList<>();
		Variable variable;
		
		while (lexer.token == Token.ID) {
			variable = new Variable(type, lexer.getStringValue());
			next();
			
			if (symbolTable.getInLocal(variable.getName()) != null) {
				error("Local variable '" + variable.getName() + "' already declared");
			}
			
			variables.add(variable);
			symbolTable.putInLocal(variable.getName(), variable);
			
			if (lexer.token == Token.COMMA) {
				next();
			}
			else {
				break;
			}
		}
		
		if (lexer.token == Token.ASSIGN) {
			next();
			// check if there is just one variable
			expr = expr();
		}
		
		return new LocalDec(type, variables, expr);
	}

	private RepeatStat repeatStat() {	
		next();
		
		List<Statement> stmtList = statementList();
		
		check(Token.UNTIL, "missing keyword 'until'");
		
		next();
		
		Expr expr = expr();
		
		if (expr.getType() != Type.booleanType) {
			error("Boolean expression expected in a repeat-until statement");
		}
		
		return new RepeatStat(stmtList, expr);
	}

	private BreakStat breakStat() {
		next();
		return new BreakStat();
	}

	private ReturnStat returnStat() {
		next();
		
		if (this.currentMethod.getReturnType() == null) {
			error("Illegal 'return' statement. Method returns 'void'");
		}
		
		Expr expr = expr();
		
		if (!isTypeValid(currentMethod.getReturnType(), expr.getType())) {
			error("Wrong return type in method '" + currentMethod.getName() + "'");
		}
		
		return new ReturnStat(expr);
	}

	private WhileStat whileStat() {
		next();
		
		Expr expr = expr();
		
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		
		next();
		
		List<Statement> stmtList = statementList();
		
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		
		next();
		
		return new WhileStat(expr, stmtList);
	}

	private IfStat ifStat() {
		List<Statement> ifStatement = null, elseStatement = null;
		
		next();
		
		Expr expr = expr();
		
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		
		next();
		
		ifStatement = statementList();
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		
		next();
		
		if (lexer.token == Token.ELSE) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			
			elseStatement = statementList();
			
			check(Token.RIGHTCURBRACKET, "'}' was expected");
			next();
		}
		
		return new IfStat(expr, ifStatement, elseStatement);
	}

	/**

	 */
	private PrintStat printStat() {
		next();
		
		check(Token.DOT, "a '.' was expected after 'Out'");
		
		next();
		
		if (lexer.token != Token.ID && lexer.token != Token.IDCOLON) {
			error("'print:' or 'println:' was expected after 'Out.'");
		}
		
		Token printType = lexer.token;
		
		next();
		
		List<Expr> exprList = new ArrayList<>();
		
		exprList.add(expr());
		
		for (Expr e : exprList) {
			if (e.getType() != Type.stringType && e.getType() != Type.intType) {
				error("Attempt to print a '" + e.getType().getName() + "' expression");
			}
		}
		
		while (lexer.token == Token.COMMA) {
			next();
			exprList.add(expr());
		}
		
		return new PrintStat(printType, exprList);
	}

	private Expr expr() {
		Expr expr = simpleExpression();
		
		if (lexer.token == Token.EQ
				|| lexer.token == Token.LT
				|| lexer.token == Token.GT
				|| lexer.token == Token.LE
				|| lexer.token == Token.GE
				|| lexer.token == Token.NEQ) {
			Token operator = lexer.token;
			next();
			expr = new CompositeExpr(expr, operator, simpleExpression());
		}

		return expr;
	}

	private FieldDec fieldDec(Qualifier qualifier) {
		next();
		Type type = type();
		Variable variable;
		List<Variable> variables = new ArrayList<>();
		
		check(lexer.token, "A field name was expected");
		
		while (lexer.token == Token.ID) {
			variable = new Variable(qualifier, type, lexer.getStringValue());
			next();
			
			if (symbolTable.getInClass(variable.getName()) != null) {
				error("Variable '" + variable.getName() + "' already declared in this scope");
			}
			
			variables.add(variable);
			symbolTable.putInClass(variable.getName(), variable);
			
			if (lexer.token == Token.COMMA) {
				next();
			}
			else {
				break;
			}
		}
		
		if (lexer.token == Token.SEMICOLON) {
			next();
		}
		
		return new FieldDec(qualifier, type, variables);
	}

	private Type type() {
		if (lexer.token == Token.INT) {
			next();
			return Type.intType;
		} else if (lexer.token == Token.BOOLEAN) {
			next();
			return Type.booleanType;
		} else if (lexer.token == Token.STRING) {
			next();
			return Type.stringType;
		}
		else if (lexer.token == Token.ID) {
			String className = lexer.getStringValue();
			next();
			
			if (symbolTable.getInGlobal(className) == null) {
				error("Class '" + className + "' does not exist");				
			}
			
			TypeCianetoClass typeCianetoClass = new TypeCianetoClass(className);
			
			return typeCianetoClass;
		}
		else {
			this.error("A type was expected");
		}
		
		return null;
	}

	private Qualifier qualifier() {
		if (lexer.token == Token.PRIVATE) {
			next();
			return new Qualifier(Token.PRIVATE);
		}
		else if (lexer.token == Token.PUBLIC) {
			next();
			return new Qualifier(Token.PUBLIC);
		}
		else if (lexer.token == Token.OVERRIDE) {
			next();
			if (lexer.token == Token.PUBLIC) {
				next();
				return new Qualifier(Token.OVERRIDE, Token.PUBLIC);
			} else {
				return new Qualifier(Token.OVERRIDE);
			}
		}
		else if (lexer.token == Token.FINAL) {
			next();
			if (lexer.token == Token.PUBLIC) {
				next();
				return new Qualifier(Token.FINAL, Token.PUBLIC);
			}
			else if (lexer.token == Token.OVERRIDE) {
				next();
				if (lexer.token == Token.PUBLIC) {
					next();
					return new Qualifier(Token.FINAL, Token.OVERRIDE, Token.PUBLIC);
				}
			} else {
				return new Qualifier(Token.FINAL);
			}
		} else if (lexer.token == Token.SHARED) {
			next();
			if (lexer.token == Token.PRIVATE) {
				next();
				return new Qualifier(Token.SHARED, Token.PRIVATE);
			} else if (lexer.token == Token.PUBLIC) {
				next();
				return new Qualifier(Token.SHARED, Token.PUBLIC);
			}
		}
		
		return null;
	}
	/**
	 * change this method to 'private'.
	 * uncomment it
	 * implement the methods it calls
	 */
	private Statement assertStat() {
		lexer.nextToken();
		Expr expr = expr();
		if (lexer.token != Token.COMMA) {
			this.error("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if (lexer.token != Token.LITERALSTRING) {
			this.error("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();

		return new AssertStat(expr, message);
	}

	private LiteralInt literalInt() {
		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}
	
	private LiteralString literalString() {
		String value = lexer.getStringValue();
		lexer.nextToken();
		return new LiteralString(value);
	}

	private Expr assignExpr() {
		Expr left = expr();
		Token operator = null;
		Expr right = null;
		
		if (lexer.token == Token.ASSIGN) {
			operator = lexer.token;
			next();
			right = expr();
			
			if (!isTypeValid(left.getType(), right.getType())) {
				error("Cannot assign expressions of different types");
			}
		}
		
		return new CompositeExpr(left, operator, right);
	}
	
	private Boolean isTypeValid(Type first, Type second) {
		if (first == null || second == null) {
			return false;
		}
		
		if ((first == Type.intType || first == Type.booleanType || first == Type.stringType) && first == second) {
			return true;
		}
		
		if ((first == Type.cianetoClassType || first == Type.stringType) && second == Type.nilType) {
			return true;
		}
		
		if (first instanceof TypeCianetoClass) {
			if (second == Type.nilType || second == Type.undefinedType) {
				return true;
			} else if (second instanceof TypeCianetoClass
					&& (second.getName().equals(first.getName())
							|| isSubclass(first.getName(), second.getName()))) {
				return true;
			}
			return false;
		}	
		
		return false;
	}
	
	private Boolean isSubclass(String parentType, String subclassType) {
		ClassDec parent = (ClassDec) symbolTable.getInGlobal(parentType);
		ClassDec sub = (ClassDec) symbolTable.getInGlobal(subclassType);
		Boolean isSubclass = false;
		ClassDec superClass = sub.getSuperclass();
		
		while (superClass != null) {
			if (parent.getName().equals(superClass.getName())) {
				isSubclass = true;
				break;
			}
			superClass = superClass.getSuperclass();
		}
		
		return isSubclass;		
	}

	private Expr basicValue() {
		if (lexer.token == Token.LITERALINT) {
			return literalInt();
		} else if (lexer.token == Token.TRUE) {
			next();
			return LiteralBoolean.True;
		} else if (lexer.token == Token.FALSE) {
			next();
			return LiteralBoolean.False;
		} else if (lexer.token == Token.LITERALSTRING) {
			return literalString();
		}
		
		error("A basic value was expected");
		
		return null;
	}

	private List<Expr> exprList() {
		List<Expr> expressionList = new ArrayList<>();
		Expr exp = expr();
		
		if (exp == null) {
			error("Expression was expected");
		}
		expressionList.add(exp);
		
		while (lexer.token == Token.COMMA) {
			next();
			exp = expr();
			if(exp == null) {
				error("Expression was expected");
			}
			expressionList.add(exp);
		}
		
		return expressionList;
	}

	private List<Variable> formalParamDec() {
		List<Variable> formalParamDec = new ArrayList<>();
		formalParamDec.add(paramDec());
		
		while (lexer.token == Token.COMMA) {
			next();
			formalParamDec.add(paramDec());
		}
		
		return formalParamDec;
	}

	private Token highOperator() {
		Token operator = null;
		
		if (lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND) {
			operator = lexer.token;
			next();
		} else {
			error("'*' or '/' or '&&' operator was expected");
		}
		
		return operator;
	}
	
	private Expr simpleExpression() {
		Expr expr = sumSubExpression();
		
		while (lexer.token == Token.PLUSPLUS) {
			next();
			expr = new CompositeExpr(expr, Token.PLUSPLUS, sumSubExpression());
		}
		
		return expr;
	}

	private Expr sumSubExpression() {
		Token operator = null;
		
		Expr left = term();
		Expr right;
		
		while (lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR) {
			operator = lowOperator();
			right = term();
			
			if (operator != Token.OR
					&& (left.getType() == Type.booleanType || right.getType() == Type.booleanType)) {
				error("Operator '" + operator.toString() + "' does not support 'Boolean' type");
			}
			
			left = new CompositeExpr(left, operator, right);
		}
		
		return left;
	}

	private Token lowOperator() {
		Token operator = null;
		
		if (lexer.token == Token.PLUS) {
			operator = lexer.token;
			next();
		} else if (lexer.token == Token.MINUS) {
			operator = lexer.token;
			next();
		} else if (lexer.token == Token.OR) {
			operator = lexer.token;
			next();
		} else {
			error("'+' or '-' or '||' was expected");
		}
		
		return operator;
	}

	private Expr term() {
		Expr expr = signalFactor();
		Token operator = null;
		
		while (lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND) {
			operator = highOperator();
			
			if (operator == Token.AND && expr.getType() == Type.intType) {
				error("Operator '&&' does not support expressions of type Int");
			}
			
			expr = new CompositeExpr(expr, operator, signalFactor());
		}
		
		return expr;
	}

	private PrefixOperatorExpr signalFactor() {
		Token signal = null;
		Expr factor = null;
		
		if (lexer.token == Token.PLUS || lexer.token == Token.MINUS) {
			signal = signal();
		}
		
		factor = factor();
		
		if (signal != null && factor.getType() != Type.intType) {
			error("Operator '" + signal.toString() + "' does not accept '" + factor.getType().getName() + "' expressions");
		}
		
		return new PrefixOperatorExpr(signal, factor);
	}
	
	private Expr factor() {
		Expr expr = null;
		if (lexer.token == Token.LITERALINT
				|| lexer.token == Token.TRUE
				|| lexer.token == Token.FALSE
				||lexer.token == Token.LITERALSTRING) {
			return basicValue();
		} else if (lexer.token == Token.LEFTPAR) {
			next();
			expr = expr();
			
			if (lexer.token == Token.RIGHTPAR) {
				next();
			} else {
				error("'}' was expected");
			}
			
			return new ParenthesisExpr(expr);
		} else if (lexer.token == Token.NOT) {
			Token operator = lexer.token;
			next();
			expr = factor();
			
			if (expr.getType() != Type.booleanType) {
				error("Operator '!' does not support expressions of type'" + expr.getType().getName() + "'");
			}
			
			
			return new PrefixOperatorExpr(operator, expr);
		} else if (lexer.token == Token.NIL) {
			next();
			return new NilExpr();
		} else if (lexer.token == Token.SUPER
				|| lexer.token == Token.ID
				|| lexer.token == Token.SELF
				|| lexer.token == Token.IN) {
			return primaryExpr();
		}
		
		return null;
	}

	private Expr primaryExpr() {
		String messageName = "";
		String receiverName = "";
		List<Expr> argList = new ArrayList<>();
		
		if (lexer.token == Token.SUPER) {
			next();
			
			ClassDec classDec = this.currentClass.getSuperclass();
			
			if (classDec == null) {
				error("Class '" + this.currentClass.getName() + "' does not have a superclass");
			}
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					messageName = lexer.getStringValue();
					next();
					
					Member member = classDec.findMember(classDec, messageName);
					
					if (member == null) {
						error("Member '" + messageName + "' not found in subclass '" + currentClass.getName() + "' or it's superclasses");
					}
					
					return new MessageSendUnaryExpr("super", member);
					
				} else if (lexer.token == Token.IDCOLON) {
					messageName = lexer.getStringValue();
					next();
					
					MethodDec method = classDec.findMethod(classDec, messageName);
					
					if (method == null) {
						error("Method '" + messageName + "' not found in subclass '" + currentClass.getName() + "' or it's superclasses");
					}
					
					argList = exprList();
					
					checkParams(method, argList);
					
					return new MessageSendKeywordExpr("super", method, argList);
					
				} else {
					error("An identifier was expected");
				}
			} else {
				error("'.' was expected");
			}
		}
		
		if (lexer.token == Token.ID) {
			receiverName = lexer.getStringValue();
			next();
			
			Variable variable = (Variable) symbolTable.getInLocal(receiverName);
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					messageName = lexer.getStringValue();
					next();
					
					if (variable == null) {
						error("Variable '" + receiverName + "' not declared");
					}
					
					ClassDec classDec = (ClassDec) symbolTable.getInGlobal(variable.getType().getName());
					
					if (classDec == null) {
						error("Class '" + messageName + "' not declared");
					}
					
					Member member = classDec.findMember(classDec, messageName);
					
					if (member == null) {
						member = (Member) symbolTable.getInCurrentScope(messageName);
						
						if (member == null) {
							error("Member '" + messageName + "' not found in class '" + classDec.getName() + "' or it's superclasses");
						} else if (!this.currentClass.getName().contentEquals(classDec.getName())) {
							error("Member '" + messageName + "' not found in class '" + classDec.getName() + "' or it's superclasses");
						}
					}
					
					return new MessageSendUnaryExpr(receiverName, member);
					
				} else if (lexer.token == Token.IDCOLON) {
					String memberName = lexer.getStringValue();
					next();
					
					if (variable == null) {
						error("Variable '" + receiverName + "' not declared");
					}
					
					ClassDec classDec = (ClassDec) symbolTable.getInGlobal(variable.getType().getName());
					
					if (classDec == null) {
						error("Class '" + messageName + "' not declared");
					}
					
					MethodDec method = classDec.findMethod(classDec, memberName);
					
					if (method == null) {
						method = (MethodDec) symbolTable.getInClass(memberName);
						
						if (method == null) {
							error("Method '" + memberName + "' not found in class '" + classDec.getName() + "' or it's superclasses");
						} else if (!this.currentClass.getName().contentEquals(classDec.getName())) {
							error("Method '" + memberName + "' not found in class '" + classDec.getName() + "' or it's superclasses");
						}
					}
					
					argList = exprList();
					
					checkParams(method, argList);
					
					return new MessageSendKeywordExpr(receiverName, method, argList);
					
				} else if (lexer.token == Token.NEW) {
					next();
					
					ClassDec classDec = (ClassDec) symbolTable.getInGlobal(receiverName);
					
					if (classDec == null) {
						error("Class '" + receiverName + "' not declared");
					}
					
					return new NewObjectExpr(classDec);
				} else {
					error("ident, ident: or 'new' keyword was expected");
				}
			}
			
			if (variable != null) {
				return variable;
			}
			
			error("Member '" + receiverName + "' not declared");
		}
		
		if (lexer.token == Token.SELF) {
			next();
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					receiverName = lexer.getStringValue();
					next();
					
					Member member = (Member) symbolTable.getInClass(receiverName);
					
					if (member == null) {
						member = currentClass.findMember(currentClass, receiverName);
					}
					
					if (member == null) {
						error("Member '" + receiverName + "' not found");
					}
					
					if (lexer.token == Token.DOT) {
						next();
						
						if (lexer.token == Token.ID) {
							messageName = lexer.getStringValue();
							next();
							
							ClassDec classDec = (ClassDec) symbolTable.getInGlobal(member.getType().getName());
							
							member = (Member) symbolTable.getInClass(messageName);
							
							if (member == null) {
								member = currentClass.findMember(classDec, messageName);
							}
							
							if (member == null) {
								error("Member '" + messageName + "' not found");
							}
							return new MessageSendUnaryToFieldExpr("self", receiverName, member);
						} else if (lexer.token == Token.IDCOLON) {
							messageName = lexer.getStringValue();
							next();
							
							ClassDec classDec = (ClassDec) symbolTable.getInGlobal(member.getType().getName());
							
							MethodDec method = (MethodDec) symbolTable.getInClass(messageName);
							
							if (method == null) {
								method = currentClass.findMethod(classDec, messageName);
							}
							
							if (method == null) {
								error("Method '" + messageName + "' not found");
							}
							
							argList = exprList();
							
							checkParams(method, argList);
							
							return new MessageSendKeywordToFieldExpr("self", receiverName, method, argList);
							
						} else {
							error("An identifier was expected");
						}
					}
					return new MessageSendUnaryExpr("self", member);
				} else if (lexer.token == Token.IDCOLON) {
					messageName = lexer.getStringValue();
					next();
					
					MethodDec method = (MethodDec) symbolTable.getInClass(messageName);
					
					if (method == null) {
						method = (MethodDec) currentClass.findMember(currentClass, messageName);
					}
					
					if (method == null) {
						error("Method '" + messageName + "' not found");
					}
					
					argList = exprList();
					
					checkParams(method, argList);
					
					return new MessageSendKeywordExpr("self", method, argList);
					
				} else {
					error("An identifier was expected");
				}
			}
		}
		
		if (lexer.token == Token.IN) {
			return readExpr();
		}
		
		return null;
	}
	
	private void checkParams(MethodDec method, List<Expr> argList) {
		List<Variable> methodParams = method.getParams();
		
		if (argList.size() != methodParams.size()) {
			error("Wrong number of params in method '" + method.getName() + "'");
		}
		
		for (int i = 0; i < argList.size(); i++) {
			if (!isTypeValid(methodParams.get(i).getType(), argList.get(i).getType())) {
				error("Wrong parameter type in method '" + method.getName() + "'");
			}
		}
	}


	private Token signal() {
		Token signal = null;
		
		if (lexer.token == Token.PLUS || lexer.token == Token.MINUS) {
			signal = lexer.token;
			next();
		} else {
			error("'+' or '-' was expected");
		}
		
		return signal;
	}
	
	private Variable paramDec() {
		Type type = type();
		
		if (lexer.token != Token.ID) {
			error("A parameter name was expected");
		}
		
		String paramName = lexer.getStringValue();
		next();
		
		Variable param = new Variable(type, paramName);
		symbolTable.putInLocal(paramName, param);
		
		return param;
	}
	
	private ReadExpr readExpr() {
		Token readType = null;
		
		if (lexer.token == Token.IN) {
			next();
		} else {
			error("'In' was expected");
		}
		
		if (lexer.token == Token.DOT) {
			next();
		} else {
			error("'.' was expected");
		}
		
		if (lexer.token == Token.READINT || lexer.token == Token.READSTRING) {
			readType = lexer.token;
			next();
		} else {
			error("A 'readInt' or 'readString' method was expected");
		}
		
		return new ReadExpr(readType);
	}	

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	private ClassDec		currentClass;
	private MethodDec		currentMethod;

}
