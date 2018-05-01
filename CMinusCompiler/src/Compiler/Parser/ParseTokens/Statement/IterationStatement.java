package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.Printable;
import Compiler.Parser.Program;
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

public class IterationStatement extends Statement implements Printable {
    Expression conditional;
    Statement statement;

    public IterationStatement(Expression conditional, Statement statement) {
        this.conditional = conditional;
        this.statement = statement;
    }

    public static IterationStatement parseIterationStatement(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(WHILE_TOKEN)) {
            nextToken = tokens.getNextToken();
            if (nextToken.match(OPEN_PARENS_TOKEN)) {
                nextToken = tokens.viewNextToken();
                if (nextToken.match(ID_TOKEN) || nextToken.match(NUM_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN)) {
                    Expression expr = Expression.parseExpression(tokens);
                    nextToken = tokens.getNextToken();
                    if (nextToken.match(CLOSE_PARENS_TOKEN)) {
                        nextToken = tokens.viewNextToken();
                        if (nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)
                                || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(IF_TOKEN) || nextToken.match(WHILE_TOKEN)
                                || nextToken.match(RETURN_TOKEN) || nextToken.match(OPEN_CURLY_BRACE_TOKEN)) {
                            Statement statement = Statement.parseStatement(tokens);
                            return new IterationStatement(expr, statement);
                        }
                        throw new ParseException("Expected statement", 0);
                    }
                    throw new ParseException("Expected statement", 0);
                }
                throw new ParseException("Expected statement", 0);
            }
            throw new ParseException("Expected statement", 0);
        }
        throw new ParseException("Expected statement", 0);
    }

    @Override
    public String print(String padding){
        String toPrint = padding + "while(\n";
        if(conditional != null){
            toPrint += conditional.print(padding + "  ");
        }
        toPrint += padding + ")\n";
        toPrint += padding + "{\n";
        if(statement != null){
            toPrint += statement.print(padding + "  ");
        }
        toPrint += padding + "}\n";
        return toPrint;
    }

    @Override
    public void genLLCode(Function function) throws IOException{
        BasicBlock whileBlock = new BasicBlock(function);
        BasicBlock fallthrough = new BasicBlock(function);
        int conditionalResultRegister = conditional.genLLCodeAndRegister(function);
        // need to get which operation type it is somehow (LTE, LE, GE, etc)
        Operation branchEq = new Operation(OperationType.BEQ, function.getCurrBlock());
        branchEq.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionalResultRegister));
        branchEq.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
        branchEq.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, fallthrough.getBlockNum()));
        function.getCurrBlock().appendOper(branchEq);

        function.appendToCurrentBlock(whileBlock);
        function.setCurrBlock(whileBlock);
        statement.genLLCode(function);
        int conditionalResultRegisterWhile = conditional.genLLCodeAndRegister(function);
        Operation branchEqWhile = new Operation(OperationType.BNE, function.getCurrBlock());
        branchEqWhile.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionalResultRegisterWhile));
        branchEqWhile.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
        branchEqWhile.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, whileBlock.getBlockNum()));
        function.getCurrBlock().appendOper(branchEqWhile);

        function.appendToCurrentBlock(fallthrough);
        function.setCurrBlock(fallthrough);
    }
}
