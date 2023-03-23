import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Read {
    public float minSup;

    public float getMinSup() {
        return minSup;
    }

    /**
     * reads through the dataset and sets the minimum support threshold
     *
     * @param arg    file path
     * @param minsup minimum support
     * @param sorted
     * @return a datatype holding the dataset
     * @throws FileNotFoundException if the file provided does not exist
     */
//    public Map<Integer, Set<Integer>> read(String arg, int minsup) throws FileNotFoundException {
//        Map<Integer, Set<Integer>> data = new HashMap<>();
//
//        File f = new File(arg);
//        Scanner sc = new Scanner(f);
//
//        minSup = sc.nextInt() * (minsup / 100f);
//
//        while (sc.hasNextInt()){
//            int orderNum = sc.nextInt();
//            int items = sc.nextInt();
//            data.put(orderNum, new HashSet<>());
//            for(int i = 0; i < items; i++){
//                data.get(orderNum).add(sc.nextInt());
//            }
//        }
//        sc.close();
//
//
//        return data;
//    }

    public Tree createFPTree(String fileName, float minsup, TreeMap<Integer, Integer> sorted) throws FileNotFoundException {
        Tree fpTree = new Tree(minSup);
        File f = new File(fileName);
        Scanner sc = new Scanner(f);
        ArrayList<Integer> transaction = new ArrayList<>();
        Set<Integer> holdRow = new HashSet<>();
        sc.nextInt();

        while (sc.hasNext())
        {
            sc.nextInt();
            int items = sc.nextInt();
            for(int i = 0; i < items; i++){
                int itemNum = sc.nextInt();
                holdRow.add(itemNum);
            }
            System.out.println("holdrow "+holdRow.toString());
            for(int i: sorted.keySet())
            {
                if(holdRow.contains(i))
                {
                    transaction.add(i);
                }
            }
            fpTree.addTransaction(transaction);
            transaction.clear();
            holdRow.clear();
        }
        sc.close();


        System.out.println(fpTree.getheaderTable().toString());

        return fpTree;
    }

        /**
         * reads through the dataset and counts each item
         * @param data a datatype holding the datatype
         * @param minSup minimum support threshold
         * @return the count of the item
         */
//    public Map<Integer, Integer> countItems(Map<Integer, Set<Integer>> data, float minSup) {
//
//        Map<Integer, Integer> count = new TreeMap<>();
//
//
//        for(int x: data.keySet())
//        {
//            Set<Integer> temp = data.get(x);
//            for(int y: temp){
//
//                if(count.get(y) == null)
//                {
//                    count.put(y, 1);
//                }
//                else {
//                    count.put(y, count.get(y)+1);
//                }
//            }
//        }
//
//        Map<Integer, Integer> temp = new TreeMap<>(count);
//
//        for(int z: temp.keySet())
//        {
//            if((float) temp.get(z) < minSup)
//            {
//                count.remove(z);
//            }
//        }
//
//        return count;
//    }


    public TreeMap<Integer, Integer> countItems(String fileName, int minsup) throws FileNotFoundException {
        File f = new File(fileName);
        TreeMap<Integer, Integer> countItems = new TreeMap<>();
        Scanner sc = new Scanner(f);
        int tempNum = 0;

        minSup = sc.nextInt() * (minsup / 100f);

        while (sc.hasNextInt()){
            int orderNum = sc.nextInt();
            int items = sc.nextInt();

            for(int i = 0; i < items; i++){
                tempNum = sc.nextInt();
                if(countItems.get(tempNum) == null)
                {
                    countItems.put(tempNum, 1);
                }
                else {
                    countItems.put(tempNum, countItems.get(tempNum)+1);
                }
            }
        }

        Iterator<Map.Entry<Integer,Integer>> iterator = countItems.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Integer,Integer> entry = iterator.next();
            if(entry.getValue() < minSup)
            {
                iterator.remove();
            }
        }

        return countItems;

    }


    /**
     * sort the count map in ascending/descending order based off of value
     * @param unsorted the count map
     * @return sorted map
     * @param <K>
     * @param <V>
     */



    }





