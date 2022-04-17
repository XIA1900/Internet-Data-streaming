import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class CountMinPaper {

    public static class Flow implements Comparable<Flow> {
        String realFlowID;
        int packetSize;
        int estimatedSize;

        public Flow(String realFlowID, int packetSize, int estimatedSize) {
            this.realFlowID = realFlowID;
            this.estimatedSize = estimatedSize;
            this.packetSize = packetSize;
        }

        public int getEstimatedSize() {
            return estimatedSize;
        }

        @Override
        public int compareTo(Flow o) {
            return o.getEstimatedSize() - this.estimatedSize;
        }
    }

    List<Flow> flowList = new ArrayList<>();
    int[][] countMinArray;
    int k;
    int w;
    int hashValue;

    public CountMinPaper(int k, int w) {
        this.k = k;
        this.w = w;
        countMinArray = new int[k][w];
    }
    double ppp=0.3;
    private void getFlow() throws IOException {
        File file = new File("project3input.txt");
        InputStreamReader read = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineText = null;
        while ((lineText = bufferedReader.readLine()) != null) {
            String realFlowID = lineText.substring(0, lineText.indexOf(" "));
            //int flowID = Math.abs(lineText.substring(0, lineText.indexOf(" ")).hashCode());
            int packetSizes = Integer.parseInt(lineText.substring(lineText.lastIndexOf(" ") + 1));
            Flow flow = new Flow(realFlowID, packetSizes, 0);
            flowList.add(flow);
        }
        read.close();
    }

    private int random() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }

    private int probability() {
        Random random = new Random();
        return random.nextInt(100);
    }

    private int Hash(String flowId) {
        int flowID = Math.abs(flowId.hashCode());
        int hashResult = flowID ^ hashValue;
        return hashResult;
    }

    int[] hashNum = new int[3];

    private void Record() {
        for (int j = 0; j < 3; j++) {
            hashValue = random();
            hashNum[j] = hashValue;
            for (int i = 0; i < flowList.size(); i++) {
                if (probability()<30) {
                    int hashResult = Hash(flowList.get(i).realFlowID);
                    countMinArray[j][hashResult % w] += flowList.get(i).packetSize;
                }
            }
        }
    }

    private void Query() {
        for (int i = 0; i < flowList.size(); i++) {
            int count = Integer.MAX_VALUE;
            for (int j = 0; j < 3; j++) {
                hashValue = hashNum[j];
                int hashResult = Hash(flowList.get(i).realFlowID);
                if (countMinArray[j][hashResult % w] < count) {
                    count = countMinArray[j][hashResult % w];
                }
            }
            flowList.get(i).estimatedSize = (int)(count/ppp);
        }
    }

    private int computeError() {
        int num = 0;
        for (int i = 0; i < flowList.size(); i++) {
            //if (flowList.get(i).estimatedSize - flowList.get(i).packetSize > 0) {
            //num += Math.abs(flowList.get(i).estimatedSize - flowList.get(i).packetSize);
                num += Math.abs(flowList.get(i).estimatedSize - flowList.get(i).packetSize);
           // }
        }
        return num / 10000;
    }

    public static void main(String[] args) throws IOException {
        CountMinPaper cm = new CountMinPaper(3, 3000);
        cm.getFlow();
        cm.Record();
        cm.Query();
        File file = new File("OutCountMinPaper.txt");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write("the average error among all flows:" + Integer.toString(cm.computeError()));
        bw.newLine();
        /*
        Collections.sort(cm.flowList);
        for (int i = 0; i < 100; i++) {
            bw.write("flowID:" + cm.flowList.get(i).realFlowID + "   ");
            bw.write("estimated size:" + Integer.toString(cm.flowList.get(i).estimatedSize) + "   ");
            bw.write("true size:" + Integer.toString((int)((cm.flowList.get(i).packetSize))));
            bw.newLine();
        }*

         */
        bw.close();

    }
}
