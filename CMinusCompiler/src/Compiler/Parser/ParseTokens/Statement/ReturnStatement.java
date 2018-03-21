package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

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
        String toPrint = padding + "ReturnStatement:\n";
        toPrint += padding + "ReturnValue{\n";
        if(expression != null){
            toPrint += expression.print(padding + "  ");
        }
        else{
            toPrint += padding + "none\n";
        }
        toPrint += padding + "}\n";
        return toPrint;
    }
}
