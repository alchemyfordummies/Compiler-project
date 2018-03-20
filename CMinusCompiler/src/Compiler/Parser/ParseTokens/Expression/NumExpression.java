package Compiler.Parser.ParseTokens.Expression;

import Compiler.Scanner.Token;

public class NumExpression extends Expression{
    Token num;
    public NumExpression(Token num){
        this.num = num;
    }
}
