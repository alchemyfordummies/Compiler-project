package Compiler.Parser;

import Compiler.Parser.ParseTokens.DeclarationList;
import ProjThreeCode.lowlevel.CodeItem;
import ProjThreeCode.lowlevel.Function;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Program {
    public static List<String> globalSymbols;
    public static int tempNum;

    public DeclarationList declarationList;
    public Program(DeclarationList declarationList){
        this.declarationList = declarationList;
    }

    public CodeItem genLLCode() throws IOException{
        globalSymbols = new ArrayList<>();
        tempNum = 1;
        return declarationList.genLLCode();
    }

    public static int lookupSymbol(String s, Function function){
        if(!function.getTable().containsKey(s)){
            int newReg = function.getNewRegNum();
            function.getTable().put(s, newReg);
        }
        return (int)function.getTable().get(s);
    }

    public static boolean isGlobalSymbol(String s){
        return globalSymbols.contains(s);
    }

    public static void addGlobalSymbol(String s){
        globalSymbols.add(s);
    }

    public static int getNextAvailableRegister(Function function){
        String id = "temp" + tempNum++;
        return lookupSymbol(id, function);
    }
}
