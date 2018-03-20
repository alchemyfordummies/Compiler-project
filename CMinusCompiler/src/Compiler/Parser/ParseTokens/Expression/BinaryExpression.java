package Compiler.Parser.ParseTokens.Expression;

import Compiler.Scanner.Token;

public class BinaryExpression extends Expression{
    Expression lhs;
    Expression rhs;
    Token operator;

    public BinaryExpression(Expression lhs, Expression rhs, Token operator){
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }
}
