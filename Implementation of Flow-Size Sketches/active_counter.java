import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.ExecutionException;

public class active_counter {
    static int number_part;
    static int expo_part;
    static int cn;
    static int ce;

    public static void main(String args[]) {
        number_part = Integer.parseInt(args[0]);
        expo_part = Integer.parseInt(args[1]);

        //number_part = 16;
        //expo_part = 16;

        cn = 0;
        ce = 0;

        int times = 1000000;
        for(int k=0; k<times; k++) {
            active_increase();
        }

        int result = cn * (int)Math.pow(2,ce);

        try {
            File writename = new File("active_counter_output.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(result+"\r\n");
            out.flush();
            out.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void active_increase() {
        double rd = Math.random();
        double prob = (double)1/Math.pow(2, ce);
        if(rd < prob) {
            cn++;
        }
        if(cn >= Math.pow(2, number_part)) {
            cn = cn >>> 1;
            ce++;
        }
    }
}
