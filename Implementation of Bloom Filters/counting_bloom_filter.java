import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class counting_bloom_filter {
    static int elements_initial;
    static int elements_removed;
    static int elements_added;
    static int counter_num;
    static int hash_num;

    public static void main(String args[]) {

        elements_initial = Integer.valueOf(args[0]);
        elements_removed = Integer.valueOf(args[1]);
        elements_added = Integer.valueOf(args[2]);
        counter_num = Integer.valueOf(args[3]);
        hash_num = Integer.valueOf(args[4]);

        //elements_initial = 1000;
        //elements_removed = 500;
        //elements_added = 500;
        //counter_num = 10000;
        //hash_num = 7;

        counting_bloom_filter();
    }

    public static void counting_bloom_filter() {
        HashSet<Integer> elements = new HashSet<>();
        HashSet<Integer> update = new HashSet<>();
        int[] counter_array = new int[counter_num];
        int[] random_nums = new int[hash_num];

        //generate elements
        while(elements.size() < elements_initial) elements.add(1 + (int)(Math.random()*100000000));
        while(update.size() < elements_added) update.add(1 + (int)(Math.random()*100000000));

        //generate random numbers
        for(int i=0; i<hash_num; i++) random_nums[i] = (int)(Math.random() * 100000000);

        //add original elements
        Iterator<Integer> ele_ori = elements.iterator();
        while(ele_ori.hasNext()) {
            int ele = ele_ori.next();
            for(int j : random_nums) {
                int counter = Integer.hashCode(j ^ ele) % counter_num;
                counter_array[counter] ++;
            }
        }

        //remove some elements
        Iterator<Integer> ele_rmv = elements.iterator();
        int removed = 0;
        while(ele_rmv.hasNext() && removed < elements_removed) {
            int ele = ele_rmv.next();
            for(int k : random_nums) {
                int counter_rmv = Integer.hashCode(k ^ ele) % counter_num;
                counter_array[counter_rmv] --;
            }
            removed++;
        }


        //add some other elements
        Iterator<Integer> ele_upd = update.iterator();
        while(ele_upd.hasNext()) {
            int ele = ele_upd.next();
            for(int m : random_nums) {
                int counter_upd = Integer.hashCode(m ^ ele) % counter_num;
                counter_array[counter_upd] ++;
            }
        }

        //look up all original elements of A
        boolean insd = true;
        int count_find = 0;
        for(int elem : elements) {
            for(int n : random_nums) {
                int counter_look = Integer.hashCode(n ^ elem) % counter_num;
                if(counter_array[counter_look] <= 0)  {
                    insd = false;
                    break;
                }
            }
            if(insd == true) count_find++;
            insd = true;
        }

        try {
            File file = new File("counting_bloom_filter_output.txt");
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(count_find + "\r\n");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
