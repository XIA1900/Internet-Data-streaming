import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class dleft_hashing {
    static int entries_num;
    static int flow_num;
    static int hash_num;
    public static void main(String args[]) {

        entries_num = Integer.parseInt(args[0]);
        flow_num = Integer.parseInt(args[1]);
        hash_num = Integer.parseInt(args[2]);


        /* //demo
        entries_num = 1000;
        flow_num = 1000;
        hash_num = 4;
        */

        dleft_hashing();
    }

    public static void dleft_hashing() {
        int[] table = new int[entries_num];
        int[] random_nums = new int[hash_num];
        int count = 0;
        HashSet<Integer> flowids = new HashSet();

        //generate random flow_ids
        while(flowids.size() < flow_num) flowids.add(1 + (int)(Math.random()*100000000));

        //generate random numbers
        for(int i=0; i<hash_num; i++) random_nums[i] = (int)(Math.random()*100000000);

        //insert
        int seg = entries_num / hash_num;
        Iterator<Integer> i = flowids.iterator();
        while(i.hasNext()) {
            int flowid = i.next();
            for(int n=0; n<hash_num; n++) {
                int hs = Integer.hashCode(flowid ^ random_nums[n]) % seg + seg * n;
                if(table[hs] == 0) {
                    table[hs] = flowid;
                    count++;
                    break;
                }
            }
        }

        try {
            File file = new File("dleft_hashing_output.txt");
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
