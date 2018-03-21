package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.text.ParseException;

import static Compiler.Scanner.Token.TokenType.*;

public class Statement implements Printable{
    public static Statement parseStatement(TokenList tokens) throws ParseException {
        Token nextToken = tokens.viewNextToken();
        if (nextToken.match(ID_TOKEN) || nextToken.match(SEMICOLON_TOKEN)) {
            return ExpressionStatement.parseExpressionStatement(tokens);
        } else if (nextToken.match(OPEN_CURLY_BRACE_TOKEN)) {
            return CompoundStatement.parseCompoundStatement(tokens);
        } else if (nextToken.match(IF_TOKEN)) {
            return SelectionStatement.parseSelectionStatement(tokens);
        } else if (nextToken.match(WHILE_TOKEN)) {
            return IterationStatement.parseIterationStatement(tokens);
        } else if (nextToken.match(RETURN_TOKEN)) {
            return ReturnStatement.parseReturnStatement(tokens);
        } else {
            throw new ParseException("Invalid token for statement", 8);
        }
    }

    @Override
    public String print(String padding){
        return "";
    }
}
