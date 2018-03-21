package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.text.ParseException;

import static Compiler.Scanner.Token.TokenType.*;

public class Declaration implements Printable{
    public static Declaration parseDeclaration(TokenList tokens) throws ParseException {
        Token typeSpecifier = tokens.getNextToken();
        Token idToken = tokens.getNextToken();
        if(idToken.match(ID_TOKEN)){
            if (typeSpecifier.match(VOID_TOKEN)) {
                return FunctionDeclaration.parseFunctionDeclarationPrime(tokens, typeSpecifier, idToken);
            } else if(typeSpecifier.match(INT_TOKEN)){
                return parseDeclarationPrime(tokens, typeSpecifier, idToken);
            }
            throw new ParseException("Expected void or int", tokens.getIndex());
        }
        throw new ParseException("Expected id", tokens.getIndex());
    }

    private static Declaration parseDeclarationPrime(TokenList tokens, Token typeIdentifier, Token id) throws ParseException {
        Token currToken = tokens.getNextToken();
        if (currToken.match(SEMICOLON_TOKEN)) {
            return new VarDeclaration(typeIdentifier, id);
        } else if (currToken.match(OPEN_BRACKET_TOKEN)) {
            Token numToken = tokens.getNextToken();
            if (!numToken.match(NUM_TOKEN)) {
                throw new ParseException("Expected num", tokens.getIndex());
            }
            return new VarDeclaration(typeIdentifier, id, numToken);
        } else if (currToken.match(OPEN_PARENS_TOKEN)) {
            tokens.ungetToken();
            return FunctionDeclaration.parseFunctionDeclarationPrime(tokens, typeIdentifier, id);
        } else {
            throw new ParseException("Expected declaration'", tokens.getIndex());
        }
    }

    @Override
    public String print(String padding){
        return padding + "DeclarationTests:";
    }
}
