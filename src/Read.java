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
     * @param arg file path
     * @param minsup minimum support
     * @return a datatype holding the dataset
     * @throws FileNotFoundException if the file provided does not exist
     */
    public Map<Integer, Set<Integer>> read(String arg, int minsup) throws FileNotFoundException {
        Map<Integer, Set<Integer>> data = new HashMap<>();

        File f = new File(arg);
        Scanner sc = new Scanner(f);

        minSup = sc.nextInt() * (minsup / 100f);

        while (sc.hasNextInt()){
            int orderNum = sc.nextInt();
            int items = sc.nextInt();
            data.put(orderNum, new HashSet<>());
            for(int i = 0; i < items; i++){
                data.get(orderNum).add(sc.nextInt());
            }
        }
        sc.close();


        return data;
    }

    /**
     * reads through the dataset and counts each item
     * @param data a datatype holding the datatype
     * @param minSup minimum support threshold
     * @return the count of the item
     */
    public Map<Integer, Integer> countItems(Map<Integer, Set<Integer>> data, float minSup) {

        Map<Integer, Integer> count = new TreeMap<>();


        for(int x: data.keySet())
        {
            Set<Integer> temp = data.get(x);
            for(int y: temp){

                if(count.get(y) == null)
                {
                    count.put(y, 1);
                }
                else {
                    count.put(y, count.get(y)+1);
                }
            }
        }

        Map<Integer, Integer> temp = new TreeMap<>(count);

        for(int z: temp.keySet())
        {
            if((float) temp.get(z) < minSup)
            {
                count.remove(z);
            }
        }

        return count;
    }


    /**
     * sort the count map in ascending/descending order based off of value
     * @param unsorted the count map
     * @return sorted map
     * @param <K>
     * @param <V>
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


    }





