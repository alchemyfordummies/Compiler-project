package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;

public class AssignmentExpression extends Expression implements Printable{
    Expression lhs;
    Expression rhs;

    public AssignmentExpression(Expression lhs, Expression rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String print(String padding){
        String toPrint = padding + "AssignmentExpression:\n";
        toPrint += padding + "LHS{\n";
        toPrint += lhs.print(padding + "  ") + padding + "}\n";
        toPrint += padding + "RHS{\n";
        toPrint += rhs.print(padding + "  ") + padding + "}\n";
        return toPrint;
    }

}
