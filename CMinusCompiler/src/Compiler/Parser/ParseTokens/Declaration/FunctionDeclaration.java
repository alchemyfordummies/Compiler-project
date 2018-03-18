package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclaration extends Declaration{
    Token typeSpecifier;
    Token id;
    List<Parameter> params;
    CompoundStatement compoundStatement;

    public static FunctionDeclaration parseFunctionDeclarationPrime(TokenList tokens, Token typeSpecifier, Token id) {
        List<Parameter> params;
        CompoundStatement compoundStatement;
        if()
    }
}
