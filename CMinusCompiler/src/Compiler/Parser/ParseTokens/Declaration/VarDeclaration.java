package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Parser.Printable;
import Compiler.Scanner.Token;

public class VarDeclaration extends Declaration implements Printable {
    Token typeSpecifier;
    Token id;
    Token arrayIndex;

    public VarDeclaration(Token typeSpecifier, Token id) {
        this.typeSpecifier = typeSpecifier;
        this.id = id;
        this.arrayIndex = null;
    }

    public VarDeclaration(Token typeSpecifier, Token id, Token arrayIndex) {
        this.typeSpecifier = typeSpecifier;
        this.id = id;
        this.arrayIndex = arrayIndex;
    }

    @Override
    public String print(String padding) {
        String toPrint = padding + typeSpecifier.printToken() + " " + id.printToken()
                + (arrayIndex == null ? "\n" : ("[" + arrayIndex.printToken()) + "]\n");
        return toPrint;
    }
}
