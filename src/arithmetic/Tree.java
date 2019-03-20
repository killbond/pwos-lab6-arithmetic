package arithmetic;

public class Tree {

    private String value;

    private Tree left;

    private Tree right;

    private Tree parent;

    public Tree()
    {
        left = null;
        right = null;
        parent = null;
        value = null;
    }

    public String getValue() {
        return value;
    }

    Tree getParent() {
        return parent;
    }

    Tree getLeft() {
        return left;
    }

    Tree getRight() {
        return right;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLeft(Tree left) {
        this.left = left;
    }

    public void setRight(Tree right) {
        this.right = right;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public Tree getRoot() {
        Tree tree = this;
        while (tree.hasParent())
        {
            tree = tree.parent;
        }
        return tree;
    }

    public boolean completed() {
        return this.hasLeft() && this.hasRight();
    }

    public void insert(Tree node) {
        node.setParent(this);
        if (!hasRight()) {
            setRight(node);
        } else if (!hasLeft()) {
            setLeft(node);
        }
    }


}
