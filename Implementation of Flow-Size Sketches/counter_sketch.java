import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class counter_sketch {
    static int n;   //number of flows
    static int k;   //number of counter arrays
    static int w;   //number of counters in each array
    static String input;
    static HashMap<String, Integer> flows;

    public static void main(String args[]) {

        input = args[0];
        k = Integer.parseInt(args[0]);
        w = Integer.parseInt(args[1]);

        //k = 3;
        //w = 3000;
        //input = "project3input.txt";
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
        counter_sketch();
    }

    public static void counter_sketch() {
        int[] random_nums = new int[k];
        int[][] arrays = new int[k][w];
        HashMap<String, Integer> query = new HashMap();

        //generate random numbers
        for(int i=0; i<k; i++) random_nums[i] = (int)(Math.random()*100000000);


        //record
        for(String key : flows.keySet()) {
            for(int j=0; j<k; j++) {
                int hash = key.hashCode() ^ random_nums[j];
                int entry = Math.abs(hash) % w;
                String bnry = Integer.toBinaryString(hash);
                int bit = 0;
                //use Hi(f)[1] as the most-significant bit;
                //if this bit is 1: bit = 1; if this bit is 0: bit = 0
                if(bnry.length() < 31) bit = 0;
                else bit = bnry.charAt(bnry.length()-31) == '1' ? 1 : 0;

                if(bit == 1)
                    arrays[j][entry] += flows.get(key);
                else
                    arrays[j][entry] -= flows.get(key);
            }
        }

        //query
        for(String key1 : flows.keySet()) {
            int[] temp = new int[k];
            for(int m=0; m<k; m++) {
                int hash1 = key1.hashCode() ^ random_nums[m];
                int entry1 = Math.abs(hash1) % w;
                String bnry1 = Integer.toBinaryString(hash1);
                int bit1 = 0;
                if(bnry1.length() < 31) bit1 = 0;
                else bit1 = bnry1.charAt(bnry1.length()-31) == '1' ? 1 : 0;

                if(bit1 == 0) temp[m] = -arrays[m][entry1];
                else temp[m] = arrays[m][entry1];
            }
            Arrays.sort(temp);
            query.put(key1, k%2==0 ? (temp[k/2]+temp[k/2-1])/2 : temp[k/2]);
        }


        //error
        int sum = 0;
        for(String key2 : flows.keySet())
            sum += Math.abs(query.get(key2) - flows.get(key2));


        //find the largest 100 estimated sizes
        PriorityQueue<String> queue = new PriorityQueue<>((o1, o2) -> query.get(o1)-query.get(o2));
        for(String key3 : query.keySet()) {
            queue.add(key3);
            if(queue.size() > 100) {
                queue.poll();
            }
        }

        try{
            File writename = new File("counter_sketch_output.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(sum / n + "\r\n");
            StringBuffer str = new StringBuffer();
            while(queue.size() > 0) {
                String top = queue.poll();
                str.insert(0, top+ "\t" + query.get(top) + "\t" + flows.get(top) + "\r\n");
            }
            out.write(str.toString());
            out.flush();
            out.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}
