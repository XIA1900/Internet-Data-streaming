import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class multi_hashing {
    static int entries_num;
    static int flow_num;
    static int hash_num;
    public static void main(String args[]) {

        entries_num = Integer.parseInt(args[0]);
        flow_num = Integer.parseInt(args[1]);
        hash_num = Integer.parseInt(args[2]);


        /*
        //demo inputs
        entries_num = 1000;
        flow_num = 1000;
        hash_num = 3;
         */

        multi_hashing();
    }

    public static void multi_hashing() {
        int[] table = new int[entries_num];
        int[] random_nums = new int[hash_num];
        int count = 0;
        HashSet<Integer> flowids = new HashSet();   //avoid duplicates

        //generate random flow_ids
        while(flowids.size() < flow_num) flowids.add(1 + (int)(Math.random()*100000000));

        //generate random numbers
        for(int i=0; i<hash_num; i++) random_nums[i] = (int)(Math.random()*100000000);

        Iterator<Integer> i = flowids.iterator();
        while(i.hasNext()) {
            int flowid = i.next();
            for(int n : random_nums) {
                int entry = Integer.hashCode(flowid ^ n) % entries_num; // H(f XOR n)
                if(table[entry] == 0) {
                    count ++;
                    table[entry] = flowid;
                    break;
                }
            }
        }

        try {
            File file = new File("multi_hashing_output.txt");
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(count+"\r\n");
            for(int k=0; k<entries_num; k++) {
                out.write(table[k]+"\r\n");
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
