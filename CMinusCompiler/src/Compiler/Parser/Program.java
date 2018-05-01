package Compiler.Parser;

import Compiler.Parser.ParseTokens.DeclarationList;
import ProjThreeCode.lowlevel.CodeItem;

import java.util.HashMap;

public class Program {
    public static HashMap<String, Integer> symbolTable;
    public static int nextReg;
    public static int tempNum;

    public DeclarationList declarationList;
    public Program(DeclarationList declarationList){
        this.declarationList = declarationList;
    }

    public CodeItem genLLCode(){
        symbolTable = new HashMap<>();
        symbolTable.put("Pass", 0);
        symbolTable.put("RetReg", 1);
        nextReg = 2;
        tempNum = 1;
        return declarationList.genLLCode();
    }

    public static int lookupSymbol(String s){
        if(!symbolTable.containsKey(s)){
            symbolTable.put(s, nextReg++);
        }
        return symbolTable.get(s);
    }

    public static int getNextAvailableRegister(){
        String id = "temp" + tempNum++;
        return lookupSymbol(id);
    }
}
