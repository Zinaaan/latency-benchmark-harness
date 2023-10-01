import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import tasks.AgronaBytesReadWriteTask;
import tasks.ChronicleBytesReadWriteTask;
import tasks.RawBytesReadWriteTask;

/**
 * @author lzn
 * @date 2023/10/01 16:56
 * @description
 */
public class BenchmarkRunner {

    public static void main(String[] args) {
        JLBHOptions lbhOptions = generateOptions(new ChronicleBytesReadWriteTask());
        JLBHOptions lbhOptions1 = generateOptions(new RawBytesReadWriteTask());
        JLBHOptions lbhOptions2 = generateOptions(new AgronaBytesReadWriteTask());
        new JLBH(lbhOptions).start();
        new JLBH(lbhOptions1).start();
        new JLBH(lbhOptions2).start();
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
