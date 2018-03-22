package Compiler.Scanner;

import java.text.ParseException;

public class Token {
    public enum TokenType {
        ELSE_TOKEN,
        IF_TOKEN,
        INT_TOKEN,
        RETURN_TOKEN,
        VOID_TOKEN,
        WHILE_TOKEN,
        PLUS_TOKEN,
        MINUS_TOKEN,
        MULTIPLY_TOKEN,
        DIVIDE_TOKEN,
        LESS_THAN_TOKEN,
        LESS_THAN_EQUALS_TOKEN,
        GREATER_THAN_TOKEN,
        GREATER_THAN_EQUALS_TOKEN,
        EQUALS_TOKEN,
        NOT_EQUALS_TOKEN,
        ASSIGNMENT_TOKEN,
        SEMICOLON_TOKEN,
        COMMA_TOKEN,
        OPEN_PARENS_TOKEN,
        CLOSE_PARENS_TOKEN,
        OPEN_BRACKET_TOKEN,
        CLOSE_BRACKET_TOKEN,
        OPEN_CURLY_BRACE_TOKEN,
        CLOSE_CURLY_BRACE_TOKEN,
        ID_TOKEN,
        NUM_TOKEN,
        END_OF_FILE,
        ERROR
    }

    private TokenType tokenType;
    private Object tokenData;

    public Token(TokenType type, Object data) {
        tokenType = type;
        tokenData = data;
    }

    public Token(TokenType type) {
        tokenType = type;
        tokenData = null;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenData(Object data){
        tokenData = data;
    }

    public String printToken(){
        switch(tokenType){
            case ELSE_TOKEN:
                return "else";
            case IF_TOKEN:
                return "if";
            case INT_TOKEN:
                return "int";
            case RETURN_TOKEN:
                return "return";
            case VOID_TOKEN:
                return "void";
            case WHILE_TOKEN:
                return "while";
            case PLUS_TOKEN:
                return "+";
            case MINUS_TOKEN:
                return "-";
            case MULTIPLY_TOKEN:
                return "*";
            case DIVIDE_TOKEN:
                return "/";
            case LESS_THAN_TOKEN:
                return "<";
            case LESS_THAN_EQUALS_TOKEN:
                return "<=";
            case GREATER_THAN_TOKEN:
                return ">";
            case GREATER_THAN_EQUALS_TOKEN:
                return ">=";
            case EQUALS_TOKEN:
                return "==";
            case NOT_EQUALS_TOKEN:
                return "!=";
            case ASSIGNMENT_TOKEN:
                return "=";
            case SEMICOLON_TOKEN:
                return ";";
            case COMMA_TOKEN:
                return ",";
            case OPEN_PARENS_TOKEN:
                return "(";
            case CLOSE_PARENS_TOKEN:
                return ")";
            case OPEN_BRACKET_TOKEN:
                return "[";
            case CLOSE_BRACKET_TOKEN:
                return "]";
            case OPEN_CURLY_BRACE_TOKEN:
                return "{";
            case CLOSE_CURLY_BRACE_TOKEN:
                return "}";
            case ID_TOKEN:
                return tokenData.toString();
            case NUM_TOKEN:
                return tokenData.toString();
            case END_OF_FILE:
                return "EOF";
            case ERROR:
                return "error";
        }
        return "";
    }

    public Object getTokenData(){
        return tokenData;
    }

    public boolean match(TokenType tokenType){
        return this.tokenType == tokenType;
    }

    public boolean assertMatch(TokenType tokenType, String data) throws ParseException{
        if(this.match(tokenType)){
            return true;
        }
        else{
            throw new ParseException("Expected " + data, 0);
        }
    }
}
