package Compiler.Parser.ParseTokens;

import Compiler.Parser.ParseTokens.Declaration.Declaration;
import Compiler.Parser.ParseTokens.Declaration.FunctionDeclaration;
import Compiler.Parser.ParseTokens.Declaration.VarDeclaration;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.CodeItem;
import ProjThreeCode.lowlevel.Function;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static Compiler.Scanner.Token.TokenType.*;

public class DeclarationList implements Printable{
    private List<Declaration> declarations;

    public DeclarationList(List<Declaration> declarations){
        this.declarations = declarations;
    }

    public static DeclarationList parseDeclarationList(TokenList tokens) throws ParseException {
        List<Declaration> declList = new ArrayList<Declaration>();
        Token nextToken = tokens.viewNextToken();

        while (nextToken.match(VOID_TOKEN) || nextToken.match(INT_TOKEN)) {
            declList.add(Declaration.parseDeclaration(tokens));
            nextToken = tokens.viewNextToken();
        }

        if (declList.size() == 0) {
            throw new ParseException("Found no declarations", tokens.getIndex());
        } else {
            if(nextToken.match(END_OF_FILE)){
                return new DeclarationList(declList);
            }
            throw new ParseException("Expected EOF", tokens.getIndex());
        }
    }

    @Override
    public String print(String padding){
        String toPrint = padding + "Program:\n";
        toPrint += padding + "{\n";
        for(Declaration decl : declarations){
            toPrint += decl.print(padding + "  ");
        }
        toPrint += padding + "}\n";
        return toPrint;
    }

    public CodeItem genLLCode() throws IOException{
        Declaration firstDecl = declarations.get(0);
        CodeItem firstItem = getCodeItemForDeclaration(firstDecl);
        CodeItem currentItem = firstItem;
        for(int i = 1; i < declarations.size(); i++){
            CodeItem nextItem = getCodeItemForDeclaration(declarations.get(i));
            currentItem.setNextItem(nextItem);
            currentItem = nextItem;
        }
        return firstItem;
    }

    public CodeItem getCodeItemForDeclaration(Declaration declaration) throws IOException{
        CodeItem toReturn;
        if(declaration.getClass().equals(VarDeclaration.class)){
            toReturn = ((VarDeclaration)declaration).genDataLLCode();
        }
        else{
            toReturn = ((FunctionDeclaration)declaration).genFunctionLLCode();
        }
        return toReturn;
    }
}
