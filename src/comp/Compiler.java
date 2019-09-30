/*
 *	Isabela Salmeron Boschi	- 552593
 *	Luciane da Silva Lopes	- 552348
 */

package comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import ast.LiteralInt;
import ast.MetaobjectAnnotation;
import ast.Program;
import ast.Statement;
import ast.TypeCianetoClass;
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
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		boolean thereWasAnError = false;
		while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT ) {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				classDec();
			}
			catch( CompilerError e) {
				// if there was an exception, there is a compilation error
				thereWasAnError = true;
			}
			catch ( RuntimeException e ) {
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
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				error("Annotation 'cep' takes three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				error("The second and third parameters of annotation 'cep' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )
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

	private void classDec() {
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			// open class
		}
		if ( lexer.token != Token.CLASS ) error("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Token.ID )
			error("Identifier expected");
		String className = lexer.getStringValue();
		lexer.nextToken();
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID )
				error("Identifier expected");
			String superclassName = lexer.getStringValue();

			lexer.nextToken();
		}

		memberList();
		if ( lexer.token != Token.END)
			error("'end' expected");
		lexer.nextToken();

	}

	private void memberList() {
		while ( true ) {
			qualifier();
			if ( lexer.token == Token.VAR ) {
				fieldDec();
			}
			else if ( lexer.token == Token.FUNC ) {
				methodDec();
			}
			else {
				break;
			}
		}
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

	private void methodDec() {
		lexer.nextToken();
		if ( lexer.token == Token.ID ) {
			// unary method
			lexer.nextToken();

		}
		else if ( lexer.token == Token.IDCOLON ) {
			// keyword method. It has parameters

		}
		else {
			error("An identifier or identifer: was expected after 'func'");
		}
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			type();
		}
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		next();
		statementList();
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'{' expected");
		}
		next();

	}

	private void statementList() {
		  // only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
	}

	private void statement() {
		boolean checkSemiColon = true;
		switch ( lexer.token ) {
		case IF:
			ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			returnStat();
			break;
		case BREAK:
			breakStat();
			break;
		case SEMICOLON:
			next();
			checkSemiColon = false;
			break;
		case REPEAT:
			repeatStat();
			break;
		case VAR:
			localDec();
			break;
		case ASSERT:
			assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				printStat();
			}
			else {
				assignExpr();
			}

		}
		if ( checkSemiColon ) {
			check(Token.SEMICOLON, "';' expected");
		}
	}

	private void localDec() {
		next();
		type();
		check(Token.ID, "A variable name was expected");
		while ( lexer.token == Token.ID ) {
			next();
			if ( lexer.token == Token.COMMA ) {
				next();
			}
			else {
				break;
			}
		}
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			expr();
		}

	}

	private void repeatStat() {
		next();
		while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
		check(Token.UNTIL, "missing keyword 'until'");
	}

	private void breakStat() {
		next();

	}

	private void returnStat() {
		next();
		expr();
	}

	private void whileStat() {
		next();
		expr();
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
	}

	private void ifStat() {
		next();
		expr();
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				statement();
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
		}
	}

	/**

	 */
	private void printStat() {
		next();
		check(Token.DOT, "a '.' was expected after 'Out'");
		next();
		if (lexer.token == Token.PRINT || lexer.token == Token.PRINTLN) {
			next();
		} else {
			error("'print' or 'println' was expected after 'Out.'");
		}
		String printName = lexer.getStringValue();
		expr();
	}

	private void expr() {
		simpleExpression();
		
		if (lexer.token == Token.EQ
				|| lexer.token == Token.LT
				|| lexer.token == Token.GT
				|| lexer.token == Token.LE
				|| lexer.token == Token.GE
				|| lexer.token == Token.NEQ) {
			next();
			simpleExpression();
		}

	}

	private void fieldDec() {
		lexer.nextToken();
		type();
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		}
		else {
			while ( lexer.token == Token.ID  ) {
				lexer.nextToken();
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
				}
				else {
					break;
				}
			}
		}

	}

	private void type() {
		if ( lexer.token == Token.INT || lexer.token == Token.BOOLEAN || lexer.token == Token.STRING ) {
			next();
		}
		else if ( lexer.token == Token.ID ) {
			next();
		}
		else {
			this.error("A type was expected");
		}

	}

	private void qualifier() {
		if ( lexer.token == Token.PRIVATE ) {
			next();
		}
		else if ( lexer.token == Token.PUBLIC ) {
			next();
		}
		else if ( lexer.token == Token.OVERRIDE ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
		}
		else if ( lexer.token == Token.FINAL ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
			else if ( lexer.token == Token.OVERRIDE ) {
				next();
				if ( lexer.token == Token.PUBLIC ) {
					next();
				}
			}
		}
	}
	/**
	 * change this method to 'private'.
	 * uncomment it
	 * implement the methods it calls
	 */
	public Statement assertStat() {

		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		expr();
		if ( lexer.token != Token.COMMA ) {
			this.error("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Token.LITERALSTRING ) {
			this.error("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		if ( lexer.token == Token.SEMICOLON )
			lexer.nextToken();

		return null;
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
	
	private static boolean startExpr(Token token) {

		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NIL
				|| token == Token.ID || token == Token.LITERALSTRING;

	}

	private void assignExpr() {
		expr();
		if(lexer.token == Token.ASSIGN){
			next();
			expr();
		}
	}

	private void basicValue() {
		if (lexer.token == Token.LITERALINT
				|| lexer.token == Token.TRUE
				|| lexer.token == Token.FALSE
				||lexer.token == Token.LITERALSTRING) {
			next();
		} else {
			error("A basic value was expected");
		}
	}

	private void compStatement() {
		check(Token.LEFTCURBRACKET, "'{' was expected");
		next();

		while (lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END) {
			statement();
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
	}

	private void exprList() {
		expr();
		
		while (lexer.token == Token.COMMA) {
			next();
			expr();
		}
	}

	private void formalParamDec() {
		paramDec();
		
		while (lexer.token == Token.COMMA) {
			next();
			paramDec();
		}
	}

	private void highOperator() {
		if (lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND) {
			next();
		} else {
			error("'*' or '/' or '&&' operator was expected");
		}
	}
	
	private void simpleExpression() {
		sumSubExpression();
		
		while (lexer.token == Token.PLUS) {
			next();
			if (lexer.token == Token.PLUS) {
				next();
				sumSubExpression();
			}
		}
		
	}

	private void sumSubExpression() {
		term();
		
		while (lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR) {
			lowOperator();
			term();
		}
		
	}

	private void lowOperator() {
		if (lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR) {
			next();
		} else {
			error("'+' or '-' or '||' was expected");
		}
	}

	private void term() {
		signalFactor();
		
		while (lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND) {
			highOperator();
			signalFactor();
		}
	}

	private void signalFactor() {
		if (lexer.token == Token.PLUS || lexer.token == Token.MINUS) {
			signal();
		}
		
		factor();
	}
	
	private void factor() {
		if (lexer.token == Token.LITERALINT
				|| lexer.token == Token.TRUE
				|| lexer.token == Token.FALSE
				||lexer.token == Token.LITERALSTRING) {
			next();
		} else if (lexer.token == Token.LEFTPAR) {
			next();
			expr();
			
			if (lexer.token == Token.RIGHTPAR) {
				next();
			} else {
				error("'}' was expected");
			}
		} else if (lexer.token == Token.NOT) {
			next();
			factor();
		} else if (lexer.token == Token.NIL) {
			next();
		} else if (lexer.token == Token.SUPER
				|| lexer.token == Token.ID
				|| lexer.token == Token.SELF
				|| lexer.token == Token.IN) {
			primaryExpr();
			
			if (lexer.token == Token.NEW) {
				next();
			}
		}
	}

	private void primaryExpr() {
		if (lexer.token == Token.SUPER) {
			next();
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					next();
				} else if (lexer.token == Token.IDCOLON) {
					next();
					exprList();
				} else {
					error("An identifier was expected");
				}
			} else {
				error("'.' was expected");
			}
		}
		
		if (lexer.token == Token.ID) {
			next();
			
			if (lexer.token == Token.DOT) {
				next();
				
				if (lexer.token == Token.ID) {
					next();
				} else if (lexer.token == Token.IDCOLON) {
					next();
					exprList();
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
						} else if (lexer.token == Token.IDCOLON) {
							next();
							exprList();
						} else {
							error("An identifier was expected");
						}
					}
				} else if (lexer.token == Token.IDCOLON) {
					next();
					exprList();
				} else {
					error("An identifier was expected");
				}
			}
		}
		
		if (lexer.token == Token.IN) {
			next();
			readExpr();
		}
	}


	private void signal() {
		if (lexer.token == Token.PLUS || lexer.token == Token.MINUS) {
			next();
		} else {
			error("'+' or '-' was expected");
		}
	}
	
	private void paramDec() {
		type();
		
		if (lexer.token == Token.ID) {
			next();
		} else {
			error("A parameter name was expected");
		}
	}
	
	private void readExpr() {
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
			next();
		} else {
			error("A 'readInt' or 'readString' method was expected");
		}
	}
	
	private void objectCreation() {
		if (lexer.token == Token.ID) {
			next();
		} else {
			error("A class name was expected");
		}
		
		if (lexer.token == Token.DOT) {
			next();
		} else {
			error("'.' was expected");
		}
		
		if (lexer.token == Token.NEW) {
			next();
		} else {
			error("'new' keyword was expected");
		}
	}	

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;

}
