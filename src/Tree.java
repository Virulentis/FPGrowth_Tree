import java.util.ArrayList;
import java.util.HashMap;

/**
 * Nodes that hold the children, parent, frequency, and link
 */
class Node{
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

    /**
     * Adds a new node if the node is not already there
     * if it is it increments the frequency of the node
     * @param nodeN Node to be added
     * @return the node that was updated/added
     */
    public Node addChild(Node nodeN)
    {

        if (!children.containsKey(nodeN.name)) {
            children.put(nodeN.name, nodeN);
        }
        else {
            nodeN = children.get(nodeN.name);
            nodeN.incFrequency();
            children.put(nodeN.name, nodeN);
        }
        return nodeN;

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

    public void incFrequency() {
        frequency++;
    }

    public void setLink(Node link) {
        if(link != this)
        {
            this.link = link;
        }
    }

}

/**
 * The growth tree that holds the root and header table.
 */
public class Tree {
    private Node root;
    private HashMap<Integer, Node> headerTable;

    public HashMap<Integer, Node> getheaderTable() {
        return headerTable;
    }

    public Tree() {
        root = new Node(-1, null);
        headerTable = new HashMap<>();
    }


    /**
     * Adds all the items listed to the tree as well as the header table.
     * @param transaction list of items that are to be added to the tree.
     */
    public void addTransaction(ArrayList<Integer> transaction) {
        Node temp = root;


        for (Integer integer : transaction) {
            temp = temp.addChild(new Node(integer, temp));

            //this is to set the header tables links so that you can find every iteration of an item through the header table
            //and getlink
            if (headerTable.get(temp.getName()) != null) {
                if (temp.frequency < 2) {
                    Node linkFind = headerTable.get(temp.getName());
                    while (linkFind.getLink() != null) {
                        linkFind = linkFind.getLink();
                    }
                    linkFind.setLink(temp);
                }

            } else {
                headerTable.put(temp.getName(), temp);
            }

        }
    }
}


