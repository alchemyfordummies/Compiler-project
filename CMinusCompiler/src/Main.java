import Compiler.JflexScanner.JFlexScanner;
import Compiler.Parser.ParseTokens.Declaration.Declaration;
import Compiler.Parser.ParseTokens.DeclarationList;
import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.TokenList;
import Compiler.Scanner.CMinusScanner;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
        File cminusFile = new File(currentDir + "\\ParseTest2.cm");
        CMinusScanner scanner = new CMinusScanner(cminusFile);
        scanner.scan();
        scanner.printAllTokens();
        TokenList tokens = new TokenList(scanner.getTokensFound());
        try{
            DeclarationList program = DeclarationList.parseDeclarationList(tokens);
            String toPrint = program.print("");
            System.out.print(toPrint);
            PrintWriter writer = new PrintWriter("out.txt", "UTF-8");
            writer.print(toPrint);
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
