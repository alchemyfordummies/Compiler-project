package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.text.ParseException;

import static Compiler.Scanner.Token.TokenType.*;

public class Declaration {
    public static Declaration parseDeclaration(TokenList tokens) throws ParseException {
        Token typeSpecifier = tokens.getNextToken();
        Token idToken = tokens.getNextToken();

        if (!(typeSpecifier.match(VOID_TOKEN) || typeSpecifier.match(INT_TOKEN)) || idToken.match(ID_TOKEN)) {
            throw new ParseException("PARSE ERROR", 5);
        }
        if (typeSpecifier.match(VOID_TOKEN)) {
            return FunctionDeclaration.parseFunctionDeclarationPrime(tokens, typeSpecifier, idToken);
        } else {
            return parseDeclarationPrime(tokens, typeSpecifier, idToken);
        }
    }

    public static Declaration parseDeclarationPrime(TokenList tokens, Token typeIdentifier, Token id) throws ParseException {
        Token currToken = tokens.getNextToken();
        if (currToken.match(SEMICOLON_TOKEN)) {
            return new VarDeclaration(typeIdentifier, id);
        } else if (currToken.match(OPEN_BRACKET_TOKEN)) {
            Token numToken = tokens.getNextToken();
            if (!numToken.match(NUM_TOKEN)) {
                throw new ParseException("PARSE ERROR", 5);
            }
            return new VarDeclaration(typeIdentifier, id, numToken);
        } else if (currToken.match(OPEN_PARENS_TOKEN)) {
            tokens.ungetToken();
            return FunctionDeclaration.parseFunctionDeclarationPrime(tokens, typeIdentifier, id);
        } else {
            throw new ParseException("PARSE ERROR", 5);
        }
    }
}
