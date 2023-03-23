import java.util.ArrayList;
import java.util.HashMap;

class Node {
    // ... (rest of the code remains unchanged)

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
}

public class Tree {
    // ... (rest of the code remains unchanged)

    public void addTransaction(ArrayList<Integer> transaction) {
        Node currentNode = root;

        for (int item : transaction) {
            currentNode = currentNode.addChild(item);

            if (!headerTable.containsKey(item)) {
                headerTable.put(item, currentNode);
            } else {
                Node linkFind = headerTable.get(item);
                while (linkFind.getLink() != null && linkFind.getLink() != currentNode) {
                    linkFind = linkFind.getLink();
                }
                if (linkFind.getLink() == null) {
                    linkFind.setLink(currentNode);
                }
            }
        }
    }
}
