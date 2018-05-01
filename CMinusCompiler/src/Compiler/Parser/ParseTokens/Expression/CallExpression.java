package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Parser.Program;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.Attribute;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operand;
import ProjThreeCode.lowlevel.Operand.*;
import ProjThreeCode.lowlevel.Operation;
import ProjThreeCode.lowlevel.Operation.*;

import java.io.IOException;
import java.util.List;

public class CallExpression extends Expression implements Printable {
    Token functionToCall;
    List<Expression> argList;

    public CallExpression(Token functionToCall, List<Expression> args) {
        this.functionToCall = functionToCall;
        this.argList = args;
    }

    @Override
    public String print(String padding) {
        String toPrint = padding + functionToCall.getTokenData() + "(";
        if (!argList.isEmpty()) {
            toPrint += "\n";
            for (Expression e : argList) {
                toPrint += e.print(padding + "  ");
                if(argList.indexOf(e) != argList.size() - 1){
                    toPrint += padding + ",\n";
                }
            }
        }
        toPrint += padding + ")\n";
        return toPrint;
    }

    @Override
    public void genLLCode(Function function) throws IOException{
        for(Expression expr : argList){
            boolean isNum = false;
            int val;
            if(expr.getClass().equals(NumExpression.class)){
                isNum = true;
                val = ((NumExpression)expr).getValue();
            }
            else{
                val = expr.genLLCodeAndRegister(function);
            }
            Operation passOp = new Operation(OperationType.PASS, function.getCurrBlock());
            passOp.setSrcOperand(0, new Operand(isNum ? OperandType.INTEGER : OperandType.REGISTER, val));
            passOp.addAttribute(new Attribute("PARAM_NUM", Integer.toString(argList.indexOf(expr))));
            function.getCurrBlock().appendOper(passOp);
        }
        Operation callOp = new Operation(OperationType.CALL, function.getCurrBlock());
        callOp.setSrcOperand(0, new Operand(OperandType.STRING, functionToCall.getTokenData()));
        callOp.addAttribute(new Attribute("numParams", Integer.toString(argList.size())));
        function.getCurrBlock().appendOper(callOp);
        Operation getReturn = new Operation(OperationType.ASSIGN, function.getCurrBlock());
        getReturn.setSrcOperand(0, new Operand(OperandType.MACRO, "RetReg"));
        int tempReg = Program.getNextAvailableRegister(function);
        getReturn.setDestOperand(0, new Operand(OperandType.REGISTER, tempReg));
        function.getCurrBlock().appendOper(getReturn);
    }

    @Override
    public int genLLCodeAndRegister(Function function) throws IOException{
        for(Expression expr : argList){
            boolean isNum = false;
            int val;
            if(expr.getClass().equals(NumExpression.class)){
                isNum = true;
                val = ((NumExpression)expr).getValue();
            }
            else{
                val = expr.genLLCodeAndRegister(function);
            }
            Operation passOp = new Operation(OperationType.PASS, function.getCurrBlock());
            passOp.setSrcOperand(0, new Operand(isNum ? OperandType.INTEGER : OperandType.REGISTER, val));
            passOp.addAttribute(new Attribute("PARAM_NUM", Integer.toString(argList.indexOf(expr))));
            function.getCurrBlock().appendOper(passOp);
        }
        Operation callOp = new Operation(OperationType.CALL, function.getCurrBlock());
        callOp.setSrcOperand(0, new Operand(OperandType.STRING, functionToCall.getTokenData()));
        callOp.addAttribute(new Attribute("numParams", Integer.toString(argList.size())));
        function.getCurrBlock().appendOper(callOp);
        Operation getReturn = new Operation(OperationType.ASSIGN, function.getCurrBlock());
        getReturn.setSrcOperand(0, new Operand(OperandType.MACRO, "RetReg"));
        int tempReg = Program.getNextAvailableRegister(function);
        getReturn.setDestOperand(0, new Operand(OperandType.REGISTER, tempReg));
        function.getCurrBlock().appendOper(getReturn);
        return tempReg;
    }
}
