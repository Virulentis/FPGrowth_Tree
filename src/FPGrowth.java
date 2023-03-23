import java.io.FileNotFoundException;
import java.util.*;

public class FPGrowth {


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
    public void start(String arg, Integer minsup) throws FileNotFoundException {
        Read read = new Read();
        Map<Integer, Set<Integer>> data = read.read(arg, minsup);
        float minSup = read.getMinSup();
        Map<Integer, Integer> unsorted = read.countItems(arg, minsup);
        System.out.println(unsorted.toString());
        TreeMap<Integer, Integer> sorted = sortByValues(unsorted);
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
        HashMap<Set<Integer>, Integer> frequentItems = new HashMap<Set<Integer>, Integer>();




        for(Map.Entry<Integer, Integer> entry : sortedMap.descendingMap().entrySet())
        {
            int temp = entry.getKey();
            Node header = fpTree.getheaderTable().get(temp);
            frequentItems.put(Collections.singleton(entry.getKey()), entry.getValue());
            List<Integer> prefix = new ArrayList<>();
            makeCondTree(header, prefix, frequentItems, minSup);


        }
        System.out.println("\n\n|FPs| "+frequentItems.size()+"\n");

        for(Map.Entry<Set<Integer>, Integer> f : frequentItems.entrySet())
        {

            System.out.println(f.getKey()+" : "+f.getValue());
        }
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
        LinkedHashMap<Integer,Integer> sortedCount = new LinkedHashMap<>();
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

            Set<Integer> FIbuffer = new HashSet<>(prefix);
            Integer[] aKeys = sortedCount.keySet().toArray(new Integer[sortedCount.size()]);


//            while(conditionalTree.getheaderTable().size() > 2)
//            {
//                header = conditionalTree.getheaderTable().remove(aKeys[aKeys.length-1]);
//                System.out.println(header.name);
//                makeCondTree(header, prefix, frequentItems, minSup);
//            }
//
//
//            for(Map.Entry<Integer, Integer> i: sortedCount.entrySet())
//            {
//                FIbuffer.add(i.getKey());
//                frequentItems.put(FIbuffer, i.getValue());
//                FIbuffer = new HashSet<>(prefix);
//            }


            if (conditionalTree.getheaderTable().size() < 2 ) {

                if(sortedCount.size() > 0)
                {
                    FIbuffer.add(aKeys[aKeys.length-1]);
                    frequentItems.put(FIbuffer, aKeys[aKeys.length-1]);
                    System.out.println(FIbuffer.toString()+" size > 0");
                }


            }
            else
            {
                header = conditionalTree.getheaderTable().get(aKeys[aKeys.length-1]);
//                frequentItems.add(prefix);

                for(Map.Entry<Integer, Integer> i: sortedCount.entrySet())
                {
                    FIbuffer.add(i.getKey());
                    frequentItems.put(FIbuffer, i.getValue());
                    FIbuffer = new HashSet<>(prefix);
                }



                makeCondTree(header, prefix, frequentItems, minSup);
            }




        }
    }


}

