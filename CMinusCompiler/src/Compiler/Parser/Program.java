package Compiler.Parser;

import Compiler.Parser.ParseTokens.DeclarationList;
import ProjThreeCode.lowlevel.CodeItem;

public class Program {
    public DeclarationList declarationList;
    public Program(DeclarationList declarationList){
        this.declarationList = declarationList;
    }

    public CodeItem genLLCode(){
        return null;
    }
}
