package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;

public class AssignmentExpression extends Expression implements Printable {
    Expression lhs;
    Expression rhs;

    public AssignmentExpression(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String print(String padding) {
        String toPrint = padding + "=\n";
        toPrint += lhs.print(padding + "  ");
        toPrint += rhs.print(padding + "  ");
        return toPrint;
    }

}
