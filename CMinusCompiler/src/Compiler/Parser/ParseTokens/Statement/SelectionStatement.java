package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.ParseTokens.Expression.NumExpression;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.BasicBlock;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operand;
import ProjThreeCode.lowlevel.Operation;
import ProjThreeCode.lowlevel.Operand.*;
import ProjThreeCode.lowlevel.Operation.*;

import java.io.IOException;
import java.text.ParseException;

import static Compiler.Scanner.Token.TokenType.*;

public class SelectionStatement extends Statement implements Printable{
    Expression conditional;
    Statement doIf;
    Statement doElse;

    public SelectionStatement(Expression conditional, Statement doIf, Statement doElse) {
        this.conditional = conditional;
        this.doIf = doIf;
        this.doElse = doElse;
    }

    public static SelectionStatement parseSelectionStatement(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.assertMatch(IF_TOKEN, "if")) {
            nextToken = tokens.getNextToken();
            if (nextToken.assertMatch(OPEN_PARENS_TOKEN, "( token")) {
                nextToken = tokens.viewNextToken();
                if (nextToken.match(ID_TOKEN) || nextToken.match(NUM_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN)) {
                    Expression expr = Expression.parseExpression(tokens);
                    nextToken = tokens.getNextToken();
                    if (nextToken.assertMatch(CLOSE_PARENS_TOKEN, ")")) {
                        nextToken = tokens.viewNextToken();
                        if (nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)
                                || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(IF_TOKEN) || nextToken.match(WHILE_TOKEN)
                                || nextToken.match(RETURN_TOKEN) || nextToken.match(OPEN_CURLY_BRACE_TOKEN)) {
                            Statement ifs = Statement.parseStatement(tokens);
                            nextToken = tokens.viewNextToken();
                            if (nextToken.match(ELSE_TOKEN)) {
                                tokens.getNextToken();
                                nextToken = tokens.viewNextToken();
                                if (nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)
                                        || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(IF_TOKEN) || nextToken.match(WHILE_TOKEN)
                                        || nextToken.match(RETURN_TOKEN) || nextToken.match(OPEN_CURLY_BRACE_TOKEN)) {
                                    Statement els = Statement.parseStatement(tokens);
                                    return new SelectionStatement(expr, ifs, els);
                                }
                            } else {
                                return new SelectionStatement(expr, ifs, null);
                            }
                        }
                        throw new ParseException("Expected statement", 0);
                    }
                    throw new ParseException("Expected )", 0);
                }
                throw new ParseException("Expected expression", 0);
            }
            throw new ParseException("Expected (", 0);
        }
        throw new ParseException("Expected if", 0);
    }

    @Override
    public String print(String padding){
        String toPrint = padding + "if(\n";
        if(conditional != null){
            toPrint += conditional.print(padding + "  ");
        }
        toPrint += padding + ")\n";
        toPrint += padding + "{\n";
        if(doIf != null){
            toPrint += doIf.print(padding + "  ");
        }
        toPrint += padding + "}\n";
        toPrint += padding + "else{\n";
        if(doElse != null){
            toPrint += doElse.print(padding + "  ");
        }
        toPrint += padding + "}\n";
        return toPrint;
    }

    @Override
    public void genLLCode(Function function) throws IOException{
        BasicBlock thenBlock = new BasicBlock(function);
        BasicBlock postBlock = new BasicBlock(function);
        BasicBlock elseBlock = new BasicBlock(function);


        int conditionalResultRegister = conditional.genLLCodeAndRegister(function);
        Operation branchEq = new Operation(OperationType.BEQ, function.getCurrBlock());
        branchEq.setSrcOperand(0, new Operand(OperandType.REGISTER, conditionalResultRegister));
        branchEq.setSrcOperand(1, new Operand(OperandType.INTEGER, 0));
        branchEq.setSrcOperand(2, new Operand(OperandType.BLOCK, elseBlock.getBlockNum()));
        function.getCurrBlock().appendOper(branchEq);

        function.appendToCurrentBlock(thenBlock);
        function.setCurrBlock(thenBlock);
        doIf.genLLCode(function);

        Operation jumpToPostThen = new Operation(OperationType.JMP, thenBlock);
        jumpToPostThen.setSrcOperand(0, new Operand(OperandType.BLOCK, postBlock.getBlockNum()));
        thenBlock.appendOper(jumpToPostThen);

        function.setCurrBlock(elseBlock);
        if(doElse != null){
            doElse.genLLCode(function);
        }
        Operation jumpToPostElse = new Operation(OperationType.JMP, elseBlock);
        jumpToPostElse.setSrcOperand(0, new Operand(OperandType.BLOCK, postBlock.getBlockNum()));
        elseBlock.appendOper(jumpToPostElse);
        if(function.getFirstUnconnectedBlock() == null){
            function.setFirstUnconnectedBlock(elseBlock);
        }
        else {
            function.appendUnconnectedBlock(elseBlock);
        }

        function.appendBlock(postBlock);
        function.setCurrBlock(postBlock);
    }
}
