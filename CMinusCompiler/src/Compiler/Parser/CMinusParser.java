package Compiler.Parser;

import Compiler.Parser.ParseTokens.DeclarationList;
import Compiler.Scanner.CMinusScanner;

import java.io.File;
import java.io.PrintWriter;

public class CMinusParser implements Parser {
    public Program program;
    public TokenList tokens;

    public CMinusParser(String filename){
        File cminusFile = new File(filename);
        CMinusScanner scanner = new CMinusScanner(cminusFile);
        scanner.scan();
        scanner.printAllTokens();
        tokens = new TokenList(scanner.getTokensFound());
    }

    @Override
    public Program parse(){
        program = null;
        try{
            DeclarationList declList = DeclarationList.parseDeclarationList(tokens);
            String toPrint = declList.print("");
            PrintWriter writer = new PrintWriter("out.txt", "UTF-8");
            writer.print(toPrint);
            writer.close();
            program = new Program(declList);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return program;
    }

    @Override
    public void printAST(Program program){
        System.out.print(program.declarationList.print(""));
    }
}
