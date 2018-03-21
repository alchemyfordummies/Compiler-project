package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;

public class VarExpression extends Expression implements Printable {
    Token id;
    Expression index;

    public VarExpression(Token id, Expression index) {
        this.id = id;
        this.index = index;
    }

    @Override
    public String print(String padding) {
        String toPrint = padding + "VarExpression:\n";
        toPrint += padding + "ID{" + id.getTokenData() + "}\n";
        toPrint += padding + "Index{\n";
        if (index != null) {
            toPrint += index.print(padding + "  ");
        } else {
            toPrint += padding + "none\n";
        }
        toPrint += padding + "}\n";
        return toPrint;
    }
}
