import Compiler.JflexScanner.JFlexScanner;
import Compiler.Scanner.CMinusScanner;

import java.io.File;
import java.nio.file.Paths;

public class Program {
    public static void main(String[] args) {
        String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
        File cminusFile = new File(currentDir + "\\cminustest2.cm");
        JFlexScanner scanner = new JFlexScanner(cminusFile);
        scanner.scan();
        scanner.printAllTokens();
    }
}
