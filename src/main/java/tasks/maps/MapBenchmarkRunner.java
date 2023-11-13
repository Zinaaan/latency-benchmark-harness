package tasks.maps;

import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;

/**
 * @author lzn
 * @date 2023/10/01 16:56
 * @description
 */
public class MapBenchmarkRunner {

    public static void main(String[] args) {
        JLBHOptions lbhOptions = generateOptions(new AgronaCounterMapReadWriteTask());
        JLBHOptions lbhOptions1 = generateOptions(new JDKCounterMapReadWriteTask());
        JLBHOptions lbhOptions2 = generateOptions(new AgronaStringMapReadWriteTask());
        JLBHOptions lbhOptions3 = generateOptions(new JDKStringMapReadWriteTask());
        JLBHOptions lbhOptions4 = generateOptions(new JavaMapVsChronicleMap());
//        new JLBH(lbhOptions).start();
//        new JLBH(lbhOptions1).start();
//        new JLBH(lbhOptions2).start();
        new JLBH(lbhOptions4).start();
    }

    private static JLBHOptions generateOptions(JLBHTask task) {
        return new JLBHOptions()
                .runs(3)
                .warmUpIterations(10000)
                .iterations(10000)
                .throughput(50000)
                .accountForCoordinatedOmission(true)
                .recordOSJitter(true)
                .jlbhTask(task);
    }
}
