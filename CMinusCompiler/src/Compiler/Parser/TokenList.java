package Compiler.Parser;

import Compiler.Scanner.Token;

import java.util.List;

public class TokenList {
    List<Token> tokens;
    int index;

    public TokenList(List<Token> tokens){
        this.tokens = tokens;
        this.index = 0;
    }

    public Token getNextToken(){
        Token currToken = tokens.get(index);
        index++;
        return currToken;
    }

    public Token viewNextToken(){
        return tokens.get(index);
    }

    public void ungetToken(){
        index--;
    }
}
