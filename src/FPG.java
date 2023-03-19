import java.io.FileNotFoundException;

public class FPG {
    public static void main(String[] args) throws FileNotFoundException {
        int minSup = 0;
        Stopwatch sc = new Stopwatch();
        sc.start();

        FPGrowth fpg = new FPGrowth();

//        if(args.length != 2 )
//        {
//            System.out.println("Please provide two arguments, the filepath and the minimum support threshold");
//            System.exit(0);
//
//        }
//        else
//        {
//            minSup = Integer.parseInt(args[1]);
//            if(minSup < 0 || minSup > 100)
//            {
//                System.out.println("Minimum support must be between 0-100");
//                System.exit(0);
//            }
//            fpg.start(args[0], minSup);
//        }
        fpg.start("data.txt", 50);

        sc.stop();
        System.out.println(sc.getSec());
        System.out.println("Finished");




    }
}