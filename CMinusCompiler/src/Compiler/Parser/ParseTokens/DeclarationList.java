package Compiler.Parser.ParseTokens;

import Compiler.Parser.ParseTokens.Declaration.Declaration;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.text.ParseException;
import java.util.ArrayList;

import static Compiler.Scanner.Token.TokenType.*;

public class DeclarationList implements Printable{
    private static ArrayList<Declaration> declarations = new ArrayList<>();

    public static ArrayList<Declaration> parseDeclarationList(TokenList tokens) throws ParseException {
        Token nextToken = tokens.viewNextToken();

        while (nextToken.match(VOID_TOKEN) || nextToken.match(INT_TOKEN)) {
            declarations.add(Declaration.parseDeclaration(tokens));
            nextToken = tokens.viewNextToken();
        }

        if (declarations.size() == 0) {
            throw new ParseException("Found no declarations", tokens.getIndex());
        } else {
            if(nextToken.match(END_OF_FILE)){
                return declarations;
            }
            throw new ParseException("Expected EOF", tokens.getIndex());
        }
    }

    @Override
    public String print(String padding){
        String toPrint = padding + "DeclList:\n";
        toPrint += padding + "Declarations{\n";
        for(Declaration decl : declarations){
            toPrint += decl.print(padding + "");
            if(declarations.indexOf(decl) != declarations.size() - 1){
                toPrint += ",\n";
            }
        }
        toPrint += padding + "}\n";
        return toPrint;
    }
}
