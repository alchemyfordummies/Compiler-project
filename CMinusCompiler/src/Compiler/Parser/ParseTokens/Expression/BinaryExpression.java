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
        String toPrint = padding + "BinaryExpression:\n";
        toPrint += padding + "Operator{" + operator.getTokenType().toString() + "}\n";
        toPrint += padding + "LHS{\n";
        toPrint += lhs.print(padding + "  ") + padding + "}\n";
        toPrint += padding + "RHS{\n";
        toPrint += rhs.print(padding + "  ") + padding + "}\n";
        return toPrint;
    }
}
