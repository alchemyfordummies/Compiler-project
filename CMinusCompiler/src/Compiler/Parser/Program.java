package Compiler.Parser;

import Compiler.Parser.ParseTokens.DeclarationList;
import ProjThreeCode.lowlevel.CodeItem;

import java.util.HashMap;

public class Program {
    public static HashMap<String, Integer> symbolTable;
    public static int nextReg;

    public DeclarationList declarationList;
    public Program(DeclarationList declarationList){
        this.declarationList = declarationList;
    }

    public CodeItem genLLCode(){
        symbolTable = new HashMap<>();
        symbolTable.put("PASS", 0);
        symbolTable.put("RETREG", 1);
        nextReg = 2;
        return declarationList.genLLCode();
    }

    public static int lookupSymbol(String s){
        if(!symbolTable.containsKey(s)){
            symbolTable.put(s, nextReg++);
        }
        return symbolTable.get(s);
    }
}
