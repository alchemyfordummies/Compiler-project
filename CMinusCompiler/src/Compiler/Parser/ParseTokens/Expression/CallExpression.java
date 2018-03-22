package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;

import java.util.List;

public class CallExpression extends Expression implements Printable {
    Token functionToCall;
    List<Expression> argList;

    public CallExpression(Token functionToCall, List<Expression> args) {
        this.functionToCall = functionToCall;
        this.argList = args;
    }

    @Override
    public String print(String padding) {
        String toPrint = padding + functionToCall.getTokenData() + "(";
        if (!argList.isEmpty()) {
            toPrint += "\n";
            for (Expression e : argList) {
                toPrint += e.print(padding + "  ");
                if(argList.indexOf(e) != argList.size() - 1){
                    toPrint += padding + ",\n";
                }
            }
        }
        toPrint += padding + ")\n";
        return toPrint;
    }
}
