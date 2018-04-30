package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operand;
import ProjThreeCode.lowlevel.Operation;

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
    public void genLLCode(Function function){
        Operation oper = new Operation(Operation.OperationType.ASSIGN, function.getCurrBlock());
        Operand left = lhs.genLLOperand();
        Operand right = rhs.genLLOperand();
        oper.setDestOperand(0, left);
        oper.setSrcOperand(0, right);
        function.getCurrBlock().appendOper(oper);
    }
}
