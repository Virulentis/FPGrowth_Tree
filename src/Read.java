import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Read {
    public float minSup;

    /**
     * writes to the file MiningResult.txt the frequent patterns in no
     * specified order.
     * @param frequentItems the item(s) that are above the minimum support threshold.
     * @throws IOException
     */
    public static void outputFP(HashMap<Set<Integer>, Integer> frequentItems) throws IOException {
        File fileOut = new File("MiningResult.txt");
        FileWriter myWriter = new FileWriter(fileOut);
        myWriter.write("|FPs| = "+ frequentItems.size());

        for(Map.Entry<Set<Integer>, Integer> f : frequentItems.entrySet())
        {
            myWriter.write("\n"+f.getKey()+" : "+f.getValue());
        }
        myWriter.close();

    }

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
     * Counts each individual items frequency and removes the items that
     * are under the minimum support threshold
     * @param fileName name of the file where the dataset is
     * @param minsup mininmum support threshold
     * @return map containing the item number and the frequency of the item
     * @throws FileNotFoundException if the file does not exist at the location
     */
    public Map<Integer, Integer> countItems(String fileName, int minsup) throws FileNotFoundException {
        File f = new File(fileName);
        TreeMap<Integer, Integer> countItems = new TreeMap<>();
        Scanner sc = new Scanner(f);
        int tempNum;

        minSup = sc.nextInt() * (minsup / 100f);

        while (sc.hasNextInt()){
            sc.nextInt();
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

        countItems.entrySet().removeIf(entry -> entry.getValue() < minSup);

        return countItems;

    }

}





