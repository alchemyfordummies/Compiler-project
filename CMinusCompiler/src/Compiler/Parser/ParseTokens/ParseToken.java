package Compiler.Parser.ParseTokens;

import Compiler.Scanner.Token;
import Tree.TreeNode;

import java.util.ArrayList;

public interface ParseToken {
    public TreeNode parseToken(ArrayList<Token> tokens, int index);
    public void match(Token token);
}
