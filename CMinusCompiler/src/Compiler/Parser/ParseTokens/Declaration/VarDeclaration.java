package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Parser.Printable;
import Compiler.Parser.Program;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.Data;
import com.sun.prism.PixelFormat;

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

    public Data genDataLLCode(){
        addToSymbolTable();
        return new Data(Data.TYPE_INT, (String)id.getTokenData());
    }

    public void addToSymbolTable(){
        Program.lookupSymbol((String)id.getTokenData());
    }
}
