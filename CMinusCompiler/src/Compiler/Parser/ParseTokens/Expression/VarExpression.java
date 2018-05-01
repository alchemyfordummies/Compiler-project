package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Parser.Program;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operand;

import java.io.IOException;

public class VarExpression extends Expression implements Printable {
    Token id;
    Expression index;

    public VarExpression(Token id, Expression index) {
        this.id = id;
        this.index = index;
    }

    @Override
    public String print(String padding) {
        String toPrint = padding + id.printToken() + "\n";
        if (index != null) {
            toPrint += padding + "  [\n";
            toPrint += index.print(padding + "    ");
            toPrint += padding + "  ]\n";
        }
        return toPrint;
    }

    @Override
    public Operand genLLOperand() {
        return new Operand(Operand.OperandType.REGISTER, Program.lookupSymbol((String)id.getTokenData()));
    }

    @Override
    public void genLLCode(Function function) throws IOException{
        genLLCodeAndRegister(function);
    }

    public int genLLCodeAndRegister(Function function) throws IOException {
        return Program.lookupSymbol((String)id.getTokenData());
    }
}
