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
        String toPrint = padding + "CallExpression:\n";
        toPrint += padding + "Function{" + functionToCall.getTokenData() + "}\n";
        toPrint += padding + "Args{\n";
        if (!argList.isEmpty()) {
            for (Expression e : argList) {
                toPrint += e.print(padding + "  ");
                if(argList.indexOf(e) != argList.size() - 1){
                    toPrint += padding + ",\n";
                }
            }
        } else {
            toPrint += padding + "  none\n";
        }
        toPrint += padding + "}\n";
        return toPrint;
    }
}
