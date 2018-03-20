package Compiler.Parser.ParseTokens.Expression;

import Compiler.Scanner.Token;

public class VarExpression extends Expression{
    Token id;
    Expression index;

    public VarExpression(Token id, Expression index){
        this.id = id;
        this.index = index;
    }
}
