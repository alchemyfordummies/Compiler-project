package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Parser.Program;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operand;
import ProjThreeCode.lowlevel.Operation;

import javax.print.DocFlavor;
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
    public Operand genLLOperand(Function function) {
        return new Operand(Operand.OperandType.REGISTER, Program.lookupSymbol((String)id.getTokenData(), function));
    }

    @Override
    public void genLLCode(Function function) throws IOException{
        genLLCodeAndRegister(function);
    }

    public int genLLCodeAndRegister(Function function) {
        if(Program.globalSymbols.contains((String)id.getTokenData())){
            Operation load = new Operation(Operation.OperationType.LOAD_I, function.getCurrBlock());
            load.setSrcOperand(0, new Operand(Operand.OperandType.STRING, (String)id.getTokenData()));
            load.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
            int newTempReg = Program.getNextAvailableRegister(function);
            load.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, newTempReg));
            function.getCurrBlock().appendOper(load);
            return newTempReg;
        }
        return Program.lookupSymbol((String)id.getTokenData(), function);
    }

    public String getId(){
        return (String)id.getTokenData();
    }
}
