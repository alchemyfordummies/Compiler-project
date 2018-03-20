package Compiler.Parser;

import Compiler.Parser.ParseTokens.DeclarationList;
import Compiler.Scanner.Token;
import Tree.AbstractSyntaxTree;
import Tree.TreeNode;

import java.util.ArrayList;

public class CMinusParser implements Parser {
    private ArrayList<Token> tokens;

    public CMinusParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public AbstractSyntaxTree parse() {
//        TreeNode treeNode = new TreeNode(1);
//        Token token = tokens.get(0);
//        treeNode = declarationList.parseToken(tokens, 0);
//        if (token.getTokenType() != Token.TokenType.END_OF_FILE) {
//            // print an error
//        }
//
//        return new AbstractSyntaxTree(treeNode);
        return null;
    }
}
