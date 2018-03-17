package Tree;

public class TreeNode {
    private final int MAX_CHILDREN = 5;
    private TreeNode[] children;
    private int lineNumber;

    public TreeNode(int lineNumber) {
        this.lineNumber = lineNumber;
        children = new TreeNode[MAX_CHILDREN];
    }
}
