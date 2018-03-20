package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.ParseToken;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.text.ParseException;

import static Compiler.Scanner.Token.TokenType.*;

public class Statement {
    public static Statement parseStatement(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();

        if (nextToken.match(ID_TOKEN) || nextToken.match(SEMICOLON_TOKEN)) {
            // ExpressionStatement expressionStatement = new ExpressionStatement();
            // expressionStatement.parseExpressionStatement(tokens);
            // return expressionStatement;
        } else if (nextToken.match(OPEN_CURLY_BRACE_TOKEN)) {
            // CompoundStatement compoundStatement = new CompoundStatement();
            // compoundStatement.parseCompoundStatement(tokens);
            // return compoundStatement;
        } else if (nextToken.match(IF_TOKEN)) {
            // SelectionStatement selectionStatement = new SelectionStatement();
            // selectionStatement.parseSelectionStatement(tokens);
            // return selectionStatement;
        } else if (nextToken.match(WHILE_TOKEN)) {
            // IterationStatement iterationStatement = new IterationStatement();
            // iterationStatement.parseIterationStatement(tokens);
            // return iterationStatement;
        } else if (nextToken.match(RETURN_TOKEN)) {
            // ReturnStatement returnStatement = new ReturnStatement();
            // returnStatement.parseReturnStatement(tokens);
            // return returnStatement;
        } else {
            throw new ParseException("Invalid token for statement", 8);
        }
    }
}
