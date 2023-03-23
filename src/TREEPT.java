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

    // Other methods remain unchanged
}

public class Tree {
    private Node root;
    private HashMap<Integer, Node> headerTable;

    // Other methods and fields remain unchanged

    public void addTransaction(ArrayList<Integer> transaction) {
        Node temp = root;

        for (int j = 0; j < transaction.size(); j++) {
            int item = transaction.get(j);
            temp = temp.addChild(item);

            if (!headerTable.containsKey(item)) {
                headerTable.put(item, temp);
            } else {
                Node linkFind = headerTable.get(item);
                while (linkFind.getLink() != null && linkFind.getLink() != temp) {
                    linkFind = linkFind.getLink();
                }
                if (linkFind.getLink() == null) {
                    linkFind.setLink(temp);
                }
            }
        }
    }
}
