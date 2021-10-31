import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class coded_bloom_filter {
    static int set_num;
    static int element_num_each_set;
    static int filter_num;
    static int bits_num_each_filter;
    static int hash_num;

    public static void main(String args[]) {

        set_num = Integer.valueOf(args[0]);
        element_num_each_set = Integer.valueOf(args[1]);
        filter_num = Integer.valueOf(args[2]);
        bits_num_each_filter = Integer.valueOf(args[3]);
        hash_num = Integer.valueOf(args[4]);

        //set_num = 7;
        //element_num_each_set = 1000;
        //filter_num = 3;
        //bits_num_each_filter = 30000;
        //hash_num = 7;

        coded_bloom_filter();
    }

    public static void coded_bloom_filter() {
        int[][] filters = new int[filter_num][bits_num_each_filter];
        HashSet<Integer>[] elements = new HashSet[set_num];
        int[] random_nums = new int[hash_num];

        //generate sets of elements
        for(int i=0; i<set_num; i++) {
            elements[i] = new HashSet<>();
            while(elements[i].size() < element_num_each_set) elements[i].add(1 + (int)(Math.random()*100000000));
        }

        //generate random numbers
        for(int j=0; j<hash_num; j++) random_nums[j] = (int)(Math.random()*100000000);

        //encode and hash
        int length = (int)Math.ceil(Math.log((double)(set_num+1))/Math.log((double)2));
        for(int m=1; m<=set_num; m++) {
            HashSet<Integer> elem = elements[m-1];
            String code = Integer.toBinaryString(m);
            while(code.length() < length) code = "0" + code;
            for(int n=0; n<code.length(); n++) {
                if(code.charAt(n) == '1') {
                    for(int e : elem) {
                        for(int rd : random_nums) {
                            int entry = Integer.hashCode(rd ^ e) % bits_num_each_filter;
                            filters[n][entry] = 1;
                        }
                    }
                }
            }
        }

        //look up
        int count = 0;
        for(int p=1; p<=set_num; p++){
            HashSet<Integer> elem_ = elements[p-1];
            String right = Integer.toBinaryString(p);
            while(right.length() < length) right = "0" + right;
            for(int e : elem_) {
                String res = "";
                boolean inside = true;
                for(int q=0; q<filter_num; q++) {
                    for(int h : random_nums) {
                        int et = Integer.hashCode(h ^ e) % bits_num_each_filter;
                        if(filters[q][et] != 1) {
                            inside = false;
                            break;
                        }
                    }
                    if(inside == true) res += "1";
                    else res += "0";
                    inside = true;
                }
                if(res.equals(right)) count++;
            }
        }

        try {
            File file = new File("coded_bloom_filter_output.txt");
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(count + "\r\n");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
