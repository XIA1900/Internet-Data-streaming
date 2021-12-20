import java.io.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

public class virtual_bitmaps {
    public static int n;
    public static int m;
    public static int l;
    public static HashMap<String, Integer> flows;
    public static HashMap<String, HashSet<Integer>> elements;
    public static int[] rand;

    public static void main(String args[]) {

        input = Integer.parseInt(args[0]);
        m = Integer.parseInt(args[1]);
        l = Integer.parseInt(args[2]);

        //m = 500000;
        //l = 500;
        flows = new HashMap();
        elements = new HashMap<>();
        rand = new int[l];

        String input = "project4input.txt";
        //long total = 0;
        try {
            File filename = new File(input);
            InputStreamReader rdr = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(rdr);
            String line = br.readLine();
            if(line != null)
                n = Integer.parseInt(line);
            line = br.readLine();

            while(line != null) {
                String[] temp = line.split("\t");
                flows.put(temp[0], Integer.parseInt(temp[temp.length-1]));
                //total += flows.get(temp[0]);
                line = br.readLine();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //System.out.println(Math.log(0.2));
        virtual_bitmaps();

    }

    public static void virtual_bitmaps() {
        BitSet bitmap = new BitSet(m);

        //initialize elements of each flow into random numbers
        for(String key : flows.keySet()) {
            int num = flows.get(key);
            HashSet<Integer> eles = new HashSet();
            while(eles.size() < num) {
                eles.add((int)(Math.random()*100000000));
            }
            elements.put(key, eles);
        }

        //initialize random number array
        for(int i=0; i<l; i++) {
            rand[i] = (int)(Math.random()*100000000);
        }

        //record each element
        for(String k : flows.keySet()) {
            HashSet<Integer> e = elements.get(k);
            Iterator<Integer> ite = e.iterator();
            while (ite.hasNext()) {
                int id = Math.abs(k.hashCode() ^ rand[ite.next().hashCode() % l]) % m;
                bitmap.set(id);
                //System.out.println("set");
            }
        }


        //query
        double vb = (double)(m - bitmap.cardinality())/(double)m;
        //System.out.println("vb="+vb);

        File writeFile = new File("results.csv");
        try {
            BufferedWriter writeRes = new BufferedWriter(new FileWriter(writeFile));
            writeRes.write("actual spread,estimated spread");
            writeRes.newLine();
            for(String ky : flows.keySet()) {
                int count = 0;
                for(int i=0; i<l; i++) {
                    int d = Math.abs(ky.hashCode() ^ rand[i]) % m;
                    if(!bitmap.get(d)) count++;
                }
                double vf = (double)((double)count / (double)l);
                int nf_ = (int)(l*Math.log(vb) - l*Math.log(vf));
                if(nf_ < 0) nf_ = 0;
                writeRes.write(flows.get(ky)+","+nf_);
                writeRes.newLine();
            }
            writeRes.flush();
            writeRes.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }


    }

}
