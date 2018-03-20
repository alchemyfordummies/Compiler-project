import Compiler.JflexScanner.JFlexScanner;
import Compiler.Parser.ParseTokens.Declaration.Declaration;
import Compiler.Parser.ParseTokens.DeclarationList;
import Compiler.Parser.TokenList;
import Compiler.Scanner.CMinusScanner;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
        File cminusFile = new File(currentDir + "\\cminustest.cm");
        CMinusScanner scanner = new CMinusScanner(cminusFile);
        scanner.scan();
        scanner.printAllTokens();
        TokenList tokens = new TokenList(scanner.getTokensFound());
        try{
            List<Declaration> declarations = DeclarationList.parseDeclarationList(tokens);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
    }
}
