import java.io.FileNotFoundException;
import java.util.*;

public class FPGrowth {


    /**
     * Starts the program by reading the input, then counting it
     * afterwards it sorts it by ascending and descending
     * @param arg file path provided by user
     * @param minsup minimum support threshold
     * @throws FileNotFoundException if the file provided does not exist
     */
    public void start(String arg, Integer minsup) throws FileNotFoundException {
        Read read = new Read();
        Map<Integer, Set<Integer>> data = read.read(arg, minsup);
        float minSup = read.getMinSup();
        Map<Integer, Integer> unsorted = read.countItems(data, minSup);
        TreeMap<Integer, Integer> sorted = Read.sortByValues(unsorted);
//        TreeMap<Integer, Integer> sortedDes = Read.sortByValues(unsorted, false);

        createFPtree(data, sorted,  minSup);
    }

    /**
     * Creates the FP growth tree
     * @param data the dataset
     * @param sortedMap the count sorted by value ascending
     * @param minSup minimum support threshold
     */
    private void createFPtree(Map<Integer, Set<Integer>> data, TreeMap<Integer, Integer> sortedMap,  float minSup) {
        Node treeRoot = new Node(-1, null);
        Tree fpTree = new Tree(minSup);
        ArrayList<Integer> transaction = new ArrayList<>();


//        for(Map.Entry<Integer, Integer> temp: sortedMap.descendingMap().entrySet())
//        {
//            System.out.println(temp.getKey()+" :k, "+temp.getValue()+" :v");
//        }
//        System.out.println();
//        for(Map.Entry<Integer, Integer> temp: sortedMap.entrySet())
//        {
//            System.out.println(temp.getKey()+" :k, "+temp.getValue()+" :v");
//        }

        for(Integer x: data.keySet())
        {
            for (int i: sortedMap.keySet())
            {
                if(data.get(x).contains(i))
                {
                    transaction.add(i);
                }
            }
            fpTree.addTransaction(transaction);
            transaction.clear();
        }

        startMine(fpTree, sortedMap,  minSup);
    }

    /**
     * Starts the recursive calling of conditional tree
     * @param fpTree The main tree
     * @param sortedMap the count sorted by value ascending
     * @param minSup minimum support threshold
     */
    private void startMine(Tree fpTree, TreeMap<Integer, Integer> sortedMap,  float minSup){
        List<List<Integer>> frequentItems = new ArrayList<>();

        for(Map.Entry<Integer, Integer> entry : sortedMap.descendingMap().entrySet())
        {
            int temp = entry.getKey();
            Node header = fpTree.getheaderTable().get(temp);
            List<Integer> prefix = new ArrayList<>();
            makeCondTree(header, prefix, frequentItems, sortedMap, minSup);


        }
        System.out.println(frequentItems.toString()+"here");
    }


    /**
     * recursively calls the conditional tree until the tree is two nodes.
     * @param header The current node for
     * @param prefix All the nodes
     * @param frequentItems list of frequent items
     * @param sortedMap the count sorted by value ascending
     * @param minSup minimum support threshold.
     */
    private void makeCondTree(Node header, List<Integer> prefix, List<List<Integer>> frequentItems, TreeMap<Integer, Integer> sortedMap, float minSup) {
        ArrayList<ArrayList<Integer>> transactionList = new ArrayList<>();
        TreeMap<Integer,Integer> sortedCount = new TreeMap<>();
        int count = 0;
        int tempVal = 0;
        int nodeFrequency = 0;

        if(header != null && header.getName() != -1) {
            Tree conditionalTree = new Tree(minSup);
            prefix.add(header.getName());
            Node temp = header;

            while (temp != null && temp.getName() != -1)
            {

                transactionList.add(new ArrayList<>());
                nodeFrequency = temp.getFrequency();
                while (temp.getParent().getName() != -1)
                {
                    temp = temp.getParent();
                    transactionList.get(count).add(temp.getName());
                }
                Collections.reverse(transactionList.get(count));
                temp = header.getLink();
                header = header.getLink();


                for(int i = 1; i < nodeFrequency; i++)
                {
                    transactionList.add(transactionList.get(count));
                }


                count = transactionList.size();
            }
            for(ArrayList<Integer> tempTransaction: transactionList)
            {
                for(int i: tempTransaction)
                {
                    if(sortedCount.get(i) == null){
                        sortedCount.put(i, 1);
                    }
                    else {
                        sortedCount.put(i, sortedCount.get(i)+1);
                    }
                }
            }


            Iterator<Map.Entry<Integer,Integer>> iterator = sortedCount.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Integer,Integer> entry = iterator.next();
                if(entry.getValue() < minSup)
                {
                    iterator.remove();
                }
            }


            for(ArrayList<Integer> tempTransaction: transactionList)
            {
                Iterator<Integer> it = tempTransaction.iterator();
                while (it.hasNext())
                {
                    tempVal = it.next();
                    if(sortedCount.get(tempVal) == null){
                        it.remove();
                    }
                }
                conditionalTree.addTransaction(tempTransaction);
            }

            if (conditionalTree.getheaderTable().size() < 2) {
                prefix.addAll(conditionalTree.getheaderTable().keySet());
                frequentItems.add(prefix);


            }
            else
            {
                header = conditionalTree.getheaderTable().get(sortedCount.lastKey());
                frequentItems.add(prefix);


//                System.out.println("conditional tree "+header.name+" :H "+prefix+" p\n"+""+" FI ");
                makeCondTree(header, prefix, frequentItems, sortedCount, minSup);
            }
        }
    }


}

