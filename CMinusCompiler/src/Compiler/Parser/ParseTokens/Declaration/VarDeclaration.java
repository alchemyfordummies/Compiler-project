package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Scanner.Token;

public class VarDeclaration extends Declaration{
    Token typeSpecifier;
    Token id;
    Token arrayIndex;

    public VarDeclaration(Token typeSpecifier, Token id){
        this.typeSpecifier = typeSpecifier;
        this.id = id;
        this.arrayIndex = null;
    }

    public VarDeclaration(Token typeSpecifier, Token id, Token arrayIndex){
        this.typeSpecifier = typeSpecifier;
        this.id = id;
        this.arrayIndex = arrayIndex;
    }
}
