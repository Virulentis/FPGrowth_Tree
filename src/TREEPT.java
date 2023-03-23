import java.util.ArrayList;
import java.util.HashMap;

class Node {
    int name;
    int frequency;

    Node parent;
    Node link;
    HashMap<Integer, Node> children;

    public Node(int name, Node parent) {
        this.name = name;
        this.parent = parent;
        children = new HashMap<>();
        frequency = 1;
    }

    public Node addChild(int name) {
        Node child;
        if (children.containsKey(name)) {
            child = children.get(name);
            child.incFrequency();
        } else {
            child = new Node(name, this);
            children.put(name, child);
        }
        return child;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    public Node getLink() {
        return link;
    }

    public HashMap<Integer, Node> getChildren() {
        return children;
    }

    public void incFrequency() {
        frequency++;
    }

    public void setLink(Node link) {
        if (link != this) {
            this.link = link;
        }
    }
}

public class Tree {
    private Node root;
    private HashMap<Integer, Node> headerTable;

    public HashMap<Integer, Node> getheaderTable() {
        return headerTable;
    }

    private float minsup;

    public Tree(float minsup) {
        root = new Node(-1, null);
        headerTable = new HashMap<>();
        this.minsup = minsup;
    }

    public Node getRoot() {
        return root;
    }

    public void addTransaction(ArrayList<Integer> transaction) {
        Node temp = root;

        for (int j = 0; j < transaction.size(); j++) {
            int item = transaction.get(j);
            Node prevTemp = temp;
            temp = temp.addChild(item);

            if (prevTemp.getChildren().containsKey(item)) {
                if (!headerTable.containsKey(item)) {
                    headerTable.put(item, temp);
                } else {
                    Node linkFind = headerTable.get(item);
                    while (linkFind.getLink() != null) {
                        linkFind = linkFind.getLink();
                    }
                    if (linkFind != temp) {
                        linkFind.setLink(temp);
                    }
                }
            }
        }
    }
}
