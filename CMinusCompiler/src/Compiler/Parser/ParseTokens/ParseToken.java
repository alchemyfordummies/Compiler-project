package Compiler.Parser.ParseTokens;

import Compiler.Scanner.Token;
import Tree.TreeNode;

//test commit
public interface ParseToken {
    public TreeNode parseToken();
    public void match();
}
