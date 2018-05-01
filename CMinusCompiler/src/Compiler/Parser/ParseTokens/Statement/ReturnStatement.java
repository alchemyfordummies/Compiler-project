package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.ParseTokens.Expression.NumExpression;
import Compiler.Parser.ParseTokens.Expression.VarExpression;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.BasicBlock;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operand;
import ProjThreeCode.lowlevel.Operation;
import ProjThreeCode.lowlevel.Operation.*;

import java.io.IOException;
import java.text.ParseException;

import static Compiler.Scanner.Token.TokenType.*;

public class ReturnStatement extends Statement implements Printable{
    Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    public static ReturnStatement parseReturnStatement(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(RETURN_TOKEN)) {
            nextToken = tokens.viewNextToken();
            if (nextToken.match(SEMICOLON_TOKEN)) {
                tokens.getNextToken();
                return new ReturnStatement(null);
            } else if (nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)) {
                Expression expression = Expression.parseExpression(tokens);
                nextToken = tokens.getNextToken();
                if (nextToken.match(SEMICOLON_TOKEN)) {
                    return new ReturnStatement(expression);
                }
                throw new ParseException("Expected ;", 10);
            } else {
                throw new ParseException("Expected semicolon or expression", 0);
            }
        } else {
            throw new ParseException("Expected return", 0);
        }
    }

    @Override
    public String print(String padding){
        String toPrint = padding + "return{\n";
        if(expression != null){
            toPrint += expression.print(padding + "  ");
        }
        toPrint += padding + "}\n";
        return toPrint;
    }

    @Override
    public void genLLCode(Function function) throws IOException{
        if(expression != null){
            int val;
            boolean isNum = false;
            if(expression.getClass().equals(NumExpression.class)){
                isNum = true;
                val = ((NumExpression)expression).getValue();
            }
            else{
                val = expression.genLLCodeAndRegister(function);
            }
            Operation returnOp = new Operation(OperationType.ASSIGN, function.getCurrBlock());
            returnOp.setDestOperand(0,  new Operand(Operand.OperandType.MACRO, "RetReg"));
            returnOp.setSrcOperand(0, new Operand(isNum ? Operand.OperandType.INTEGER : Operand.OperandType.REGISTER,  val));
            function.getCurrBlock().appendOper(returnOp);
        }
        BasicBlock returnBlock = function.genReturnBlock();
        function.appendToCurrentBlock(returnBlock);
    }
}
