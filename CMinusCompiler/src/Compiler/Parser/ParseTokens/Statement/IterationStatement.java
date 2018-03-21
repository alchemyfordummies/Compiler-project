package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

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
        String toPrint = padding + "IterationStatement:\n";
        toPrint += padding + "Conditional{\n";
        if(conditional != null){
            toPrint += conditional.print(padding + "  ");
        }
        else{
            toPrint += padding + "none\n";
        }
        toPrint += padding + "}\n";
        toPrint += padding + "Statement{\n";
        if(statement != null){
            toPrint += statement.print(padding + "  ");
        }
        else{
            toPrint += "none\n";
        }
        toPrint += padding + "}\n";
        return toPrint;
    }
}
