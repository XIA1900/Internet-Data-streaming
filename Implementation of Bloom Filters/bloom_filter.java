import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class bloom_filter {
    static int elements_num;
    static int bits_num;
    static int hash_num;

    public static void main(String args[]) {

        elements_num = Integer.valueOf(args[0]);
        bits_num = Integer.valueOf(args[1]);
        hash_num = Integer.valueOf(args[2]);

        //elements_num = 1000;
        //bits_num = 10000;
        //hash_num = 7;

        bloom_filter();
    }

    public static void bloom_filter() {
        HashSet<Integer> eleA = new HashSet<>();
        HashSet<Integer> eleB = new HashSet<>();
        int[] random_nums = new int[hash_num];
        int[] filter = new int[bits_num];

        //generate random elements
        while(eleA.size() < elements_num) eleA.add(1 + (int)(Math.random()*100000000));
        while(eleB.size() < elements_num) eleB.add(1 + (int)(Math.random()*100000000));

        //generate random numbers
        for(int i=0; i<hash_num; i++) random_nums[i] = (int)(Math.random()*100000000);


        //put A
        Iterator<Integer> iA = eleA.iterator();
        while(iA.hasNext()) {
            int ele_id = iA.next();
            for(int j : random_nums) {
                int entry = Integer.hashCode(j ^ ele_id) % bits_num;
                filter[entry] = 1;
            }
        }

        //look up A
        int countA = 0;
        boolean inside = true;
        iA = eleA.iterator();
        while(iA.hasNext()) {
            int ele_id = iA.next();
            for(int k : random_nums) {
                int look = Integer.hashCode(k ^ ele_id) % bits_num;
                if(filter[look] == 0) {
                    inside = false;
                    break;
                }
            }
            if(inside == true) countA ++;
            inside = true;
        }

        //look up B
        int countB = 0;
        boolean insd = true;
        Iterator<Integer> iB = eleB.iterator();
        while(iB.hasNext()) {
            int ele_idB = iB.next();
            for(int n : random_nums) {
                int lookB = Integer.hashCode(n ^ ele_idB) % bits_num;
                if(filter[lookB] == 0) {
                    insd = false;
                    break;
                }
            }
            if(insd == true) countB ++;
            insd = true;
        }

        try {
            File file = new File("bloom_filter_output.txt");
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(countA + "\r\n");
            out.write(countB + "\r\n");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
