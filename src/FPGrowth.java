import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class FPGrowth {


    /**
     * Sort the maps items based on the value first then the key
     * @param unsorted the map that needs to be sorted
     * @return a TreeMap that is sorted by value then key
     * @param <K> key of map
     * @param <V> value of map
     */
    public static <K, V extends Comparable<V>> TreeMap<K, V> sortByValues(final Map<K, V> unsorted) {
        Comparator<K>  valueComparator;
        valueComparator = new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = unsorted.get(k2).compareTo(unsorted.get(k1));
                if (compare == 0)
                    return 1;
                else
                    return compare;
            }
        };


        TreeMap<K, V> sorted = new TreeMap<K, V>(valueComparator);
        sorted.putAll(unsorted);
        return sorted;

    }

    /**
     * Starts the program by reading the input, then counting it
     * afterwards it sorts it by ascending and descending
     * @param arg file path provided by user
     * @param minsup minimum support threshold
     * @throws FileNotFoundException if the file provided does not exist
     */
    public void start(String arg, Integer minsup) throws IOException {
        Read read = new Read();
        Map<Integer, Set<Integer>> data = read.read(arg, minsup);
        float minSup = read.getMinSup();
        Map<Integer, Integer> unsorted = read.countItems(arg, minsup);
        TreeMap<Integer, Integer> sorted = sortByValues(unsorted);
        createFPtree(data, sorted,  minSup);
    }

    /**
     * Creates the FP growth tree
     * @param data the dataset
     * @param sortedMap the count sorted by value ascending
     * @param minSup minimum support threshold
     */
    private void createFPtree(Map<Integer, Set<Integer>> data, TreeMap<Integer, Integer> sortedMap,  float minSup) throws IOException {
        Tree fpTree = new Tree();
        ArrayList<Integer> transaction = new ArrayList<>();

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
    private void startMine(Tree fpTree, TreeMap<Integer, Integer> sortedMap,  float minSup) throws IOException {
        HashMap<Set<Integer>, Integer> frequentItems = new HashMap<>();




        for(Map.Entry<Integer, Integer> entry : sortedMap.descendingMap().entrySet())
        {
            int temp = entry.getKey();
            Node header = fpTree.getheaderTable().get(temp);
            frequentItems.put(Collections.singleton(entry.getKey()), entry.getValue());
            List<Integer> prefix = new ArrayList<>();
            makeCondTree(header, prefix, frequentItems, minSup);


        }
        System.out.println("|FPs| "+frequentItems.size()+"\n");
        Read.outputFP(frequentItems);

    }


    /**
     * recursively calls the conditional tree until the tree is two nodes.
     * @param header The current node for
     * @param prefix All the nodes
     * @param frequentItems list of frequent items
     * @param minSup minimum support threshold.
     */
    private void makeCondTree(Node header, List<Integer> prefix, HashMap<Set<Integer>, Integer> frequentItems, float minSup) {
        ArrayList<ArrayList<Integer>> transactionList = new ArrayList<>();
        LinkedHashMap<Integer, Integer> sortedCount = new LinkedHashMap<>();
        int count = 0;
        int tempVal;
        int nodeFrequency = 0;


        if (header != null && header.getName() != -1) {
            Tree conditionalTree = new Tree();
            prefix.add(header.getName());
            Node temp = header;

            while (temp != null && temp.getName() != -1) {

                transactionList.add(new ArrayList<>());
                nodeFrequency = temp.getFrequency();
                while (temp.getParent().getName() != -1) {
                    temp = temp.getParent();
                    transactionList.get(count).add(temp.getName());
                }
                Collections.reverse(transactionList.get(count));
                temp = header.getLink();
                header = header.getLink();


                for (int i = 1; i < nodeFrequency; i++) {
                    transactionList.add(transactionList.get(count));
                }


                count = transactionList.size();
            }
            for (ArrayList<Integer> tempTransaction : transactionList) {
                for (int i : tempTransaction) {
                    sortedCount.merge(i, 1, Integer::sum);
                }
            }


            sortedCount.entrySet().removeIf(entry -> entry.getValue() < minSup);


            for (ArrayList<Integer> tempTransaction : transactionList) {
                Iterator<Integer> it = tempTransaction.iterator();
                while (it.hasNext()) {
                    tempVal = it.next();
                    if (sortedCount.get(tempVal) == null) {
                        it.remove();
                    }
                }
                conditionalTree.addTransaction(tempTransaction);
            }

            Set<Integer> FIbuffer = new HashSet<>(prefix);
            List<Integer> lKeys = new ArrayList<Integer>(sortedCount.keySet());
            HashMap<Integer, Node> test = conditionalTree.getheaderTable();

            // Check if there are frequent patterns in the conditional tree
            if (!conditionalTree.getheaderTable().isEmpty()) {
                while(!lKeys.isEmpty())
                {
                    header = test.get(lKeys.remove(lKeys.size()-1));

                    if (header != null) {
                        makeCondTree(header, new ArrayList<>(prefix), frequentItems, minSup);
                    }
                }


                for (Map.Entry<Integer, Integer> i : sortedCount.entrySet()) {
                    if(i.getKey() == 65)
                    {
                        System.out.println(i.getValue());
                    }
                    FIbuffer.add(i.getKey());
                    frequentItems.put(FIbuffer, i.getValue());
                    FIbuffer = new HashSet<>(prefix);
                }
            } else { // If there are no frequent patterns, add the prefix as a frequent pattern
                frequentItems.putIfAbsent(FIbuffer, nodeFrequency);
            }
        }
    }
}
