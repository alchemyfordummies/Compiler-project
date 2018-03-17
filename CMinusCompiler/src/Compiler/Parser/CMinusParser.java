package Compiler.Parser;

import Compiler.Parser.ParseTokens.DeclarationList;
import Compiler.Scanner.Token;
import Tree.AbstractSyntaxTree;
import Tree.TreeNode;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CMinusParser implements Parser {
    private ArrayList<Token> tokens;

    public CMinusParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public AbstractSyntaxTree parse() {
        TreeNode treeNode = new TreeNode(1);
        for (Token token: tokens) {
            DeclarationList declarationList = new DeclarationList(token);
            treeNode = declarationList.parseToken();
            if (token.getTokenType() != Token.TokenType.END_OF_FILE) {
                // print an error
            }
        }

        return new AbstractSyntaxTree(treeNode);
    }
}
