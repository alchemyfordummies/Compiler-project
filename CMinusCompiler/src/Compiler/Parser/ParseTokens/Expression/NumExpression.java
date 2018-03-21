package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;

public class NumExpression extends Expression implements Printable{
    Token num;
    public NumExpression(Token num){
        this.num = num;
    }

    @Override
    public String print(String padding){
        return padding + "NumExpression: {" + num.getTokenData() + "}\n";
    }
}
