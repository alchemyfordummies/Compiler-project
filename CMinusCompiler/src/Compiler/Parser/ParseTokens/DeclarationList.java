package Compiler.Parser.ParseTokens;

import Compiler.Scanner.Token;
import Tree.TreeNode;

public class DeclarationList implements ParseToken {
    private Token token;

    public DeclarationList(Token token) {
        this.token = token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public TreeNode parseToken() {
        return null;
    }
}
