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
        Operation oper;
        OperandType rhsType;
        int rValue;
        Operand lhsOper;
        Operand rhsOper;

        if(rhs.getClass().equals(NumExpression.class)) {
            rhsType = Operand.OperandType.INTEGER;
            rValue = ((NumExpression)rhs).getValue();
        }
        else{
            rhsType = Operand.OperandType.REGISTER;
            rValue = rhs.genLLCodeAndRegister(function);
        }

        if(lhs.getClass().equals(VarExpression.class) && Program.isGlobalSymbol(((VarExpression)lhs).getId())){
            oper = new Operation(OperationType.STORE_I, function.getCurrBlock());
            oper.setSrcOperand(0, new Operand(rhsType, rValue));
            oper.setSrcOperand(1, new Operand(OperandType.STRING, ((VarExpression)lhs).getId()));
            oper.setSrcOperand(2, new Operand(OperandType.INTEGER, 0));
            function.getCurrBlock().appendOper(oper);
        }
        else{
            int lValue = lhs.genLLCodeAndRegister(function);
            rhsOper = new Operand(rhsType, rValue);
            lhsOper = new Operand(OperandType.REGISTER, lValue);
            oper = new Operation(OperationType.ASSIGN, function.getCurrBlock());
            oper.setSrcOperand(0, rhsOper);
            oper.setDestOperand(0, lhsOper);
            function.getCurrBlock().appendOper(oper);
        }
    }
}
