import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;

public class countMin {
    static int n;   //number of flows
    //static int k;   //number of counter arrays
    //static int w;   //number of counters in each array
    static String input;
    static HashMap<String, Integer> flows;


    public static void main(String args[]) {

        //input = args[0];
        //int k = Integer.parseInt(args[0]);
        //w = Integer.parseInt(args[1]);


        //k = 3;
        //w = 1000;
        input = "project3input.txt";

        flows = new HashMap<>();
        try {
            File filename = new File(input);
            InputStreamReader rdr = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(rdr);
            String line = br.readLine();
            if(line != null)
                n = Integer.parseInt(line);
            line = br.readLine();
            while(line != null) {
                String[] temp = line.split(" ");
                flows.put(temp[0], Integer.parseInt(temp[temp.length-1]));
                line = br.readLine();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        double[] probs = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1};
        int[] ks = {2};
        //int[] ks = {3,4,5};
        int[] ws = {2000, 2500, 3000,3500,4000};
        //int[] ws = {3000};


        try{
            File writename = new File("sample_result.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            int round = 0;
            int[] res = new int[101];



            for(int k : ks) {
                for(int w : ws) {
                    //while(round < 50) {
                        //System.out.println(round);
                        int[] random_nums = new int[k];
                        for(int i=0; i<k; i++) random_nums[i] = (int)(Math.random()*100000000);
                        out.write("k="+k+",w="+w+"\r\n");
                        int id = 0;
                        for(double p : probs) {
                            int average = countMin(k, w, p, random_nums);
                            out.write(p+","+average+"\r\n");
                        }
                        //round++;
                    //}
                    out.write("\r\n");
                }
            }




            out.flush();
            out.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }


    }

    public static int countMin(int k, int w, double p, int[] random_nums) {
        int[][] arrays = new int[k][w];
        HashMap<String, Integer> query = new HashMap();

        //record
        for(String key : flows.keySet()) {
            int size = flows.get(key);
            int sz = 0;
            while(sz < size) {
                double prob = Math.random();
                if(prob < p) {
                    for (int j = 0; j < k; j++) {
                        int entry = (Math.abs(key.hashCode()) ^ random_nums[j]) % w;
                        arrays[j][entry] += 1;
                    }
                }
                sz++;
            }
        }

        //query
        for(String key1 : flows.keySet()) {
            int min =Integer.MAX_VALUE;
            for(int m=0; m<k; m++) {
                int entry = (Math.abs(key1.hashCode()) ^ random_nums[m]) % w;
                if(arrays[m][entry] < min) min = arrays[m][entry];
            }
            query.put(key1, (int)(min/p));
        }

        //error
        int sum = 0;
        for(String key2 : flows.keySet()) {
            sum+= Math.abs(query.get(key2) - flows.get(key2));
        }

        return sum/n;
    }
}
