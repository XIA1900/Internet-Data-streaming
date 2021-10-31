import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class cuckoo_hashing {
    static int entries_num;
    static int flow_num;
    static int hash_num;
    static int cuckoo_steps;
    public static void main(String args[]) {

        entries_num = Integer.parseInt(args[0]);
        flow_num = Integer.parseInt(args[1]);
        hash_num = Integer.parseInt(args[2]);
        cuckoo_steps = Integer.parseInt(args[3]);

        /*
        //demo
        entries_num = 1000;
        flow_num = 1000;
        hash_num = 3;
        cuckoo_steps = 2;
         */

        cuckoo_hashing();
    }

    public static void cuckoo_hashing() {
        int[] table = new int[entries_num];
        int[] hashes = new int[hash_num];
        int count = 0;
        HashSet<Integer> flowids = new HashSet();


        //generate random flows
        while(flowids.size() < flow_num) flowids.add(1 + (int)(Math.random()*100000000));

        //generate random hash functions
        for(int j=0; j<hash_num; j++)
            hashes[j] = (int)(Math.random()*100000000);

        //try to insert in hash table
        Iterator<Integer> i = flowids.iterator();
        while(i.hasNext()) {
            boolean inserted = false;
            int flowid = i.next();
            for(int n=0; n<hash_num; n++) {
                int entry = Integer.hashCode(flowid ^ hashes[n]) % entries_num;
                if(table[entry] == 0) {
                    count++;
                    table[entry] = flowid;
                    inserted = true;
                    break;
                }
            }

            //can not insert, then do the cuckoo steps
            if(inserted == false) {
                for(int k=0; k<hash_num; k++) {
                    int hf = Integer.hashCode(flowid ^ hashes[k]) % entries_num;
                    if(move(table, hashes, hf, cuckoo_steps) == true) {
                        table[hf] = flowid;
                        count++;
                        inserted = true;
                    }
                }
            }
        }
        try {
            File file = new File("cuckoo_hashing_output.txt");
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

    public static boolean move(int[] table, int[] hashes, int hf, int cuckoo_steps) {
        if(cuckoo_steps == 0) return false;
        int fid = table[hf];
        for(int h=0; h<hashes.length; h++) {
            int ent = Integer.hashCode(hashes[h] ^ fid) % entries_num;
            if(ent != hf && table[ent] == 0) {
                table[ent] = fid;
                return true;
            }
        }

        for(int t=0; t<hashes.length; t++) {
            int mv = Integer.hashCode(hashes[t] ^ fid) % entries_num;
            if(mv != hf && move(table, hashes, mv, cuckoo_steps-1) == true) {
                table[mv] = fid;
                return true;
            }
        }
        return false;

    }
}
