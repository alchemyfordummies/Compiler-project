package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Declaration.VarDeclaration;
import Compiler.Parser.ParseTokens.Expression.VarExpression;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.CodeItem;
import ProjThreeCode.lowlevel.Function;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static Compiler.Scanner.Token.TokenType.*;

public class CompoundStatement extends Statement implements Printable {
    List<VarDeclaration> localDeclarations;
    List<Statement> statementList;

    public CompoundStatement(List<VarDeclaration> localDeclarations, List<Statement> statementList) {
        this.localDeclarations = localDeclarations;
        this.statementList = statementList;
    }

    public static CompoundStatement parseCompoundStatement(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(OPEN_CURLY_BRACE_TOKEN)) {
            List<VarDeclaration> varDecls = parseLocalDeclarations(tokens);
            List<Statement> statements = parseStatementList(tokens);
            nextToken = tokens.getNextToken();
            if (nextToken.match(CLOSE_CURLY_BRACE_TOKEN)) {
                return new CompoundStatement(varDecls, statements);
            } else {
                throw new ParseException("Expected close curly brace", 0);
            }
        } else {
            throw new ParseException("Expected open curly brace", 0);
        }
    }

    public static List<VarDeclaration> parseLocalDeclarations(TokenList tokenList) throws ParseException {
        List<VarDeclaration> varDeclarations = new ArrayList<>();
        Token nextToken = tokenList.viewNextToken();
        if (nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)
                || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(IF_TOKEN) || nextToken.match(WHILE_TOKEN)
                || nextToken.match(ELSE_TOKEN) || nextToken.match(RETURN_TOKEN) || nextToken.match(CLOSE_CURLY_BRACE_TOKEN)) {
            return varDeclarations;
        } else if (nextToken.match(INT_TOKEN)) {
            while (nextToken.match(INT_TOKEN)) {
                varDeclarations.add(parseVarDeclaration(tokenList));
                nextToken = tokenList.viewNextToken();
            }
            return varDeclarations;
        } else {
            throw new ParseException("Expected first or follow of varDecl", 1);
        }
    }

    public static List<Statement> parseStatementList(TokenList tokens) throws ParseException {
        List<Statement> statements = new ArrayList<>();
        Token nextToken = tokens.viewNextToken();
        if (nextToken.match(CLOSE_CURLY_BRACE_TOKEN)) {
            return statements;
        } else {
            while (nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)
                    || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(IF_TOKEN) || nextToken.match(WHILE_TOKEN)
                    || nextToken.match(RETURN_TOKEN) || nextToken.match(OPEN_CURLY_BRACE_TOKEN)) {
                statements.add(Statement.parseStatement(tokens));
                nextToken = tokens.viewNextToken();
            }
            return statements;
        }
    }

    public static VarDeclaration parseVarDeclaration(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(INT_TOKEN)) {
            Token idToken = tokens.getNextToken();
            if (idToken.match(ID_TOKEN)) {
                Token openBracketToken = tokens.getNextToken();
                if (openBracketToken.match(OPEN_BRACKET_TOKEN)) {
                    Token numToken = tokens.getNextToken();
                    if (numToken.match(NUM_TOKEN)) {
                        if (tokens.getNextToken().match(CLOSE_BRACKET_TOKEN)) {
                            if (tokens.getNextToken().match(SEMICOLON_TOKEN)) {
                                return new VarDeclaration(nextToken, idToken, numToken);
                            }
                            throw new ParseException("Expected ;", tokens.getIndex());
                        }
                        throw new ParseException("Expected close bracket in varDecl", tokens.getIndex());
                    }
                    throw new ParseException("Expected num in varDecl index", tokens.getIndex());

                } else if (openBracketToken.match(SEMICOLON_TOKEN)){
                    return new VarDeclaration(nextToken, idToken, null);
                }
                throw new ParseException("Expected ;", tokens.getIndex());
            }
            throw new ParseException("Expected id token in varDecl", tokens.getIndex());
        }
        throw new ParseException("Expected int token in varDecl", tokens.getIndex());
    }

    @Override
    public String print(String padding) {
        String toPrint = "";
        if (!localDeclarations.isEmpty()) {
            for (VarDeclaration varDecl : localDeclarations) {
                toPrint += varDecl.print(padding + "  ");
            }
        }
        if (!statementList.isEmpty()) {
            for (Statement s : statementList) {
                toPrint += s.print(padding + "  ");
            }
        }
        return toPrint;
    }

    public void genLLCode(Function function){
        for(VarDeclaration localDecl : localDeclarations){
            localDecl.addToSymbolTable();
        }
        for(Statement stmt : statementList){
            stmt.genLLCode(function);
        }
    }
}
