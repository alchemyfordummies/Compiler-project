package Compiler.Parser.ParseTokens.Expression;

import Compiler.Scanner.Token;

import java.util.List;

public class CallExpression extends Expression {
    Token functionToCall;
    List<Expression> argList;

    public CallExpression(Token functionToCall, List<Expression> args){
        this.functionToCall = functionToCall;
        this.argList = args;
    }
}
