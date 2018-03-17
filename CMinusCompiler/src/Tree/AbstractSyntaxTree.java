package Tree;

import java.io.File;

public class AbstractSyntaxTree {
    private TreeNode Parent;

    public AbstractSyntaxTree() {
        Parent = new TreeNode(1);
    }

    public AbstractSyntaxTree(TreeNode parent) {
        Parent = parent;
    }

    public void PrintTree() {
        File treeOutput = new File("../syntaxTree.ast");
    }
}
