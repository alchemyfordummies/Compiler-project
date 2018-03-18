package Compiler.Parser.ParseTokens;

import Compiler.Parser.ParseTokens.Declaration.Declaration;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.text.ParseException;
import java.util.ArrayList;

import static Compiler.Scanner.Token.TokenType.*;

public class DeclarationList {
    private ArrayList<Declaration> declarations = new ArrayList<>();

    public ArrayList<Declaration> parseDeclarationList(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();

        while (nextToken.match(VOID_TOKEN) || nextToken.match(INT_TOKEN)) {
            Declaration declaration = new Declaration();
            declarations.add(declaration.parseDeclaration(tokens));
            nextToken = tokens.getNextToken();
        }

        if (declarations.size() == 0) {
            throw new ParseException("Found no declarations", 7);
        } else {
            return declarations;
        }
    }
}
