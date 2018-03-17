package Compiler.Parser.ParseTokens;

import Compiler.Scanner.Token;
import Tree.TreeNode;

public interface ParseToken {
    public TreeNode parseToken();
}
