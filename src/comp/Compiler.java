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
				classDec();
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
		List<MemberList> membersList = null;
		
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

			lexer.nextToken();
		}

		membersList = memberList();
		
		if ( lexer.token != Token.END)
			error("'end' expected");
		lexer.nextToken();
		
		return new ClassDec(className, superclassName, membersList);

	}

	private List<MemberList> memberList() {
		List<MemberList> memberLists = new ArrayList<>();
		MemberList memberList = null;
		Qualifier qualifier = null;
		Member member = null;
		
		while (true) {
			qualifier = qualifier();
			if (lexer.token == Token.VAR) {
				member = fieldDec(qualifier);
				memberList = new MemberList(qualifier, member);
				memberLists.add(memberList);
				
			}
			else if (lexer.token == Token.FUNC) {
				member = methodDec(qualifier);
				memberList = new MemberList(qualifier, member);
				memberLists.add(memberList);
			}
			else {
				break;
			}
		}
		
		return memberLists;
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
		List<ParamDec> formalParamDec = null;
		Type returnType = null;
		
		if (lexer.token == Token.IDCOLON) {
			// keyword method. It has parameters
			lexer.nextToken();
			formalParamDec = formalParamDec();
		} else if (lexer.token == Token.ID) {
			// unary method
			lexer.nextToken();
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
		
		List<Statement> stmtList = statementList();
		
		if (lexer.token != Token.RIGHTCURBRACKET) {
			error("'}' expected");
		}
		next();
		
		if (formalParamDec != null) {
			return new MethodDec(qualifier, methodName, formalParamDec, stmtList, returnType);
		}
		
		return new MethodDec(qualifier, methodName, stmtList, returnType);
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
		
		return new RepeatStat(stmtList, expr);
	}

	private BreakStat breakStat() {
		next();
		return new BreakStat();
	}

	private ReturnStat returnStat() {
		next();
		Expr expr = expr();
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
			TypeCianetoClass typeCianetoClass = new TypeCianetoClass(lexer.getStringValue());
			next();
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

		LiteralInt e = null;

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
	
	private static boolean startExpr(Token token) {

		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NIL
				|| token == Token.ID || token == Token.LITERALSTRING;

	}

	private Expr assignExpr() {
		Expr left = expr();
		Token operator = null;
		Expr right = null;
		
		if (lexer.token == Token.ASSIGN) {
			operator = lexer.token;
			next();
			right = expr();
		}
		
		return new CompositeExpr(left, operator, right);
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

	private void compStatement() {
		check(Token.LEFTCURBRACKET, "'{' was expected");
		next();

		while (lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END) {
			statement();
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
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

	private List<ParamDec> formalParamDec() {
		List<ParamDec> formalParamDec = new ArrayList<>();
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
		
		Expr expr = term();
		
		while (lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR) {
			operator = lowOperator();
			expr = new CompositeExpr(expr, operator, term());
		}
		
		return expr;
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
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					messageName = lexer.getStringValue();
					next();
					//return new MessageSendUnaryExpr(messageName);
				} else if (lexer.token == Token.IDCOLON) {
					next();
					argList = exprList();
					//return new MessageSendKeywordExpr(messageName, argList);
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
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					messageName = lexer.getStringValue();
					next();
					//return new MessageSendUnaryExpr(messageName);
				} else if (lexer.token == Token.IDCOLON) {
					next();
					argList = exprList();
					//return new MessageSendKeywordExpr(messageName, argList);
				} else if (lexer.token == Token.NEW) {
					next();
					//return new NewObjectExpr(receiverName);
				} else {
					error("ident, ident: or 'new' keyword was expected");
				}
			}
		}
		
		if (lexer.token == Token.SELF) {
			next();
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					next();
					
					if (lexer.token == Token.DOT) {
						next();
						
						if (lexer.token == Token.ID) {
							next();
							//return new MessageSendUnaryToFieldExpr();
						} else if (lexer.token == Token.IDCOLON) {
							next();
							argList = exprList();
							//return new MessageSendKeywordToFieldExpr();
						} else {
							error("An identifier was expected");
						}
					}
				} else if (lexer.token == Token.IDCOLON) {
					next();
					argList = exprList();
					//return new MessageSendKeywordExpr(messageName, argList);
				} else {
					error("An identifier was expected");
				}
			}
		}
		
		if (lexer.token == Token.IN) {
			return readExpr();
		}
		
		return null; // delete later
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
	
	private ParamDec paramDec() {
		Type type = type();
		
		if (lexer.token != Token.ID) {
			error("A parameter name was expected");
		}
		
		String paramName = lexer.getStringValue();
		next();
		
		return new ParamDec(type, paramName);
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

}
