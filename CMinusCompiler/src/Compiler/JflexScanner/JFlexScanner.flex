/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* Java 1.2 language lexer specification */

/* Use together with unicode.flex for Unicode preprocesssing */
/* and java12.cup for a Java 1.2 parser                      */

/* Note that this lexer specification is not tuned for speed.
   It is in fact quite slow on integer and floating point literals, 
   because the input is read twice and the methods used to parse
   the numbers are not very fast. 
   For a production quality application (e.g. a Java compiler) 
   this could be optimized */

package Compiler.JflexScanner;

import Compiler.Scanner.Scanner;
import Compiler.Scanner.Token;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
   
%%

%public
%class JFlexScanner
%implements Scanner

%unicode

%line
%column

%type Token

%{
  StringBuilder string = new StringBuilder();
  ArrayList<Token> tokensFound = new ArrayList<>();

  public JFlexScanner(File f){
        try{
            this.zzReader = new FileReader(f);
            this.tokensFound = new ArrayList<>();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
  
  private Token token(Token.TokenType type) {
    return new Token(type);
  }

  private Token token(Token.TokenType type, Object value) {
    return new Token(type, value);
  }
  
  public Token getNextToken(){
	Token t = null;
	try{
		t = yylex();
	}
	catch(IOException e){
		e.printStackTrace();
	}
	return t;
  }
  
  public Token viewNextToken(){
	Token t = null;
	try{
		t = yylex();
		printToken(t);
	}
	catch(IOException e){
		e.printStackTrace();
	}
	return t;
  }
  
  public void scan(){
        Token currentToken = getNextToken();
        tokensFound.add(currentToken);
        while (!this.zzAtEOF) {
            currentToken = getNextToken();
            tokensFound.add(currentToken);
        }
    }
	
	public void printToken(Token token){
		if (token.getTokenType() == Token.TokenType.ID_TOKEN ||
				token.getTokenType() == Token.TokenType.NUM_TOKEN) {
			System.out.println(token.getTokenType().toString() + ": " + token.getTokenData());
		} else {
			System.out.println(token.getTokenType().toString());
		}
	}

    public void printAllTokens() {
        for (Token token : tokensFound) {
            printToken(token);
        }
    }
  
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"

/* identifiers */
Identifier = [:jletter:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
    
/* string and character literals */
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

%state STRING, CHARLITERAL

%%

<YYINITIAL> {

  /* keywords */
  "else"                         { return token(Token.TokenType.ELSE_TOKEN); }
  "if"                           { return token(Token.TokenType.IF_TOKEN); }
  "int"                          { return token(Token.TokenType.INT_TOKEN); }
  "return"                       { return token(Token.TokenType.RETURN_TOKEN); }
  "void"                         { return token(Token.TokenType.VOID_TOKEN); }
  "while"                        { return token(Token.TokenType.WHILE_TOKEN); }

  /* separators */
  "("                            { return token(Token.TokenType.OPEN_PARENS_TOKEN); }
  ")"                            { return token(Token.TokenType.CLOSE_PARENS_TOKEN); }
  "{"                            { return token(Token.TokenType.OPEN_CURLY_BRACE_TOKEN); }
  "}"                            { return token(Token.TokenType.CLOSE_CURLY_BRACE_TOKEN); }
  "["                            { return token(Token.TokenType.OPEN_BRACKET_TOKEN); }
  "]"                            { return token(Token.TokenType.CLOSE_BRACKET_TOKEN); }
  ";"                            { return token(Token.TokenType.SEMICOLON_TOKEN); }
  ","                            { return token(Token.TokenType.COMMA_TOKEN); }
  
  /* operators */
  "+"                            { return token(Token.TokenType.PLUS_TOKEN); }
  "-"                            { return token(Token.TokenType.MINUS_TOKEN); }
  "*"                            { return token(Token.TokenType.MULTIPLY_TOKEN); }
  "/"                            { return token(Token.TokenType.DIVIDE_TOKEN); }
  "=="                           { return token(Token.TokenType.EQUALS_TOKEN); }
  "!="                           { return token(Token.TokenType.NOT_EQUALS_TOKEN); }
  ">"                            { return token(Token.TokenType.GREATER_THAN_TOKEN); }
  ">="                           { return token(Token.TokenType.GREATER_THAN_EQUALS_TOKEN); }
  "<"                            { return token(Token.TokenType.LESS_THAN_TOKEN); }
  "<="                           { return token(Token.TokenType.LESS_THAN_EQUALS_TOKEN); }
  "="                            { return token(Token.TokenType.ASSIGNMENT_TOKEN); }

  /* numeric literals */

  /* This is matched together with the minus, because the number is too big to 
     be represented by a positive integer. */
  "-2147483648"                  { return token(Token.TokenType.NUM_TOKEN, new Integer(Integer.MIN_VALUE)); }
  
  {DecIntegerLiteral}            { return token(Token.TokenType.NUM_TOKEN, new Integer(yytext())); }
  
  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */ 
  {Identifier}                   { return token(Token.TokenType.ID_TOKEN, yytext()); }  
}

/* error fallback */
[^]                              { return token(Token.TokenType.ERROR, yytext()); }
<<EOF>>                          { return token(Token.TokenType.END_OF_FILE); }