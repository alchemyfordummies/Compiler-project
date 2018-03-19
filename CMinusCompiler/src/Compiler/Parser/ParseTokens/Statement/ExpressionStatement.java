package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import static Compiler.Scanner.Token.TokenType.ID_TOKEN;

public class ExpressionStatement  extends Statement {
    public ExpressionStatement parseExpressionStatement(TokenList tokens) {
        Token currentToken = tokens.viewNextToken();

        if (currentToken.match(ID_TOKEN)) {
            // Expression expression = new Expression();
            // currentToken = currentToken.getNextToken();

            // if (currentToken.match(SEMICOLON_TOKEN)) {
            //     return expression.parseExpression(tokens);
            // } else {
            //     throw new ParseException("Needs a semicolon", 9);
            // }
        } else {
            return null;
        }
    }
}
