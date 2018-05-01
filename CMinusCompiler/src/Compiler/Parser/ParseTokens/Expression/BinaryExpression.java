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
import java.text.ParseException;

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

    @Override
    public void genLLCode(Function function) throws IOException {
        genLLCodeAndRegister(function);
    }

    public int genLLCodeAndRegister(Function function) throws IOException{
        OperationType type;
        switch(operator.getTokenType()){
            case ASSIGNMENT_TOKEN:
                type = OperationType.ASSIGN;
                break;
            case PLUS_TOKEN:
                type = OperationType.ADD_I;
                break;
            case MINUS_TOKEN:
                type = OperationType.SUB_I;
                break;
            case MULTIPLY_TOKEN:
                type = OperationType.MUL_I;
                break;
            case DIVIDE_TOKEN:
                type = OperationType.DIV_I;
                break;
            case LESS_THAN_TOKEN:
                type = OperationType.LT;
                break;
            case LESS_THAN_EQUALS_TOKEN:
                type = OperationType.LTE;
                break;
            case GREATER_THAN_TOKEN:
                type = OperationType.GT;
                break;
            case GREATER_THAN_EQUALS_TOKEN:
                type = OperationType.GTE;
                break;
            case NOT_EQUALS_TOKEN:
                type = OperationType.NOT_EQUAL;
                break;
            case EQUALS_TOKEN:
                type = OperationType.EQUAL;
                break;
            default:
                throw new IOException("Invalid token in BinaryExpr: " + operator.getTokenType());
        }
        Operation oper = new Operation(type, function.getCurrBlock());
        OperandType lhsType;
        int lValue;
        OperandType rhsType;
        int rValue;
        if(lhs.getClass().equals(NumExpression.class)) {
            lhsType = OperandType.INTEGER;
            lValue = ((NumExpression)lhs).getValue();
        }
        else{
            lhsType = OperandType.REGISTER;
            lValue = lhs.genLLCodeAndRegister(function);
        }
        if(rhs.getClass().equals(NumExpression.class)) {
            rhsType = OperandType.INTEGER;
            rValue = ((NumExpression)rhs).getValue();
        }
        else{
            rhsType = OperandType.REGISTER;
            rValue = rhs.genLLCodeAndRegister(function);
        }
        Operand lhsOper = new Operand(lhsType, lValue);
        Operand rhsOper = new Operand(rhsType, rValue);
        int destRegNum = Program.getNextAvailableRegister(function);
        Operand destReg = new Operand(OperandType.REGISTER, destRegNum);
        oper.setSrcOperand(0, lhsOper);
        oper.setSrcOperand(1, rhsOper);
        oper.setDestOperand(0, destReg);
        function.getCurrBlock().appendOper(oper);
        return destRegNum;
    }
}
