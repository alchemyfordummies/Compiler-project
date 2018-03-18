package Compiler.Scanner;

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

    public Object getTokenData(){
        return tokenData;
    }

    public boolean match(TokenType tokenType){
        return this.tokenType == tokenType;
    }
}
