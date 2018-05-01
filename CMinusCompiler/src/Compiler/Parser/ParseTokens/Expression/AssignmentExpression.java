package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Parser.Program;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operand;
import ProjThreeCode.lowlevel.Operand.*;
import ProjThreeCode.lowlevel.Operation;
import ProjThreeCode.lowlevel.Operation.*;

import java.io.IOException;

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

    @Override
    public void genLLCode(Function function) throws IOException{
        Operation oper = new Operation(OperationType.ASSIGN, function.getCurrBlock());
        int lValue = lhs.genLLCodeAndRegister(function);
        OperandType rhsType;
        int rValue;
        if(rhs.getClass().equals(NumExpression.class)) {
            rhsType = Operand.OperandType.INTEGER;
            rValue = ((NumExpression)rhs).getValue();
        }
        else{
            rhsType = Operand.OperandType.REGISTER;
            rValue = rhs.genLLCodeAndRegister(function);
        }
        Operand lhsOper = new Operand(OperandType.REGISTER, lValue);
        Operand rhsOper = new Operand(rhsType, rValue);
        oper.setSrcOperand(0, rhsOper);
        oper.setDestOperand(0, lhsOper);
        function.getCurrBlock().appendOper(oper);
    }
}
