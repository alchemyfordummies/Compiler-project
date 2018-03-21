package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

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
        String toPrint = padding + "IterationStatement:\n";
        toPrint += padding + "Conditional{\n";
        if(conditional != null){
            toPrint += conditional.print(padding + "  ");
        }
        else{
            toPrint += "none\n";
        }
        toPrint += "}\n";
        toPrint += padding + "DoIf{\n";
        if(doIf != null){
            toPrint += doIf.print(padding + "  ");
        }
        else{
            toPrint += "none\n";
        }
        toPrint += "}\n";
        toPrint += padding + "DoElse{\n";
        if(doElse != null){
            toPrint += doElse.print(padding + "  ");
        }
        else{
            toPrint += "none\n";
        }
        toPrint += "}\n";
        return toPrint;
    }
}
