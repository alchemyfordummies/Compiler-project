package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;

public class BinaryExpression extends Expression implements Printable {
    Expression lhs;
    Expression rhs;
    Token operator;

    public BinaryExpression(Expression lhs, Expression rhs, Token operator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public String print(String padding) {
        String toPrint = padding + operator.printToken() + "\n";
        toPrint += lhs.print(padding + "  ");
        toPrint += rhs.print(padding + "  ");
        return toPrint;
    }
}
