package tasks.bytes;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.util.NanoSampler;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;

/**
 * @author lzn
 * @date 2023/10/01 16:57
 * @description
 */
public class ChronicleBytesReadWriteTask implements JLBHTask {

    private final static Bytes<byte[]> REUSABLE_BYTES = Bytes.allocateElasticOnHeap(16);
    private JLBH jlbh;
    private NanoSampler readSampler;
    private NanoSampler writeSampler;

    @Override
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        readSampler = jlbh.addProbe("Chronicle Read Bytes");
        writeSampler = jlbh.addProbe("Chronicle Write Bytes");
    }

    @Override
    public void run(long startTime) {
        long startForWrite = System.nanoTime();
        writeData();
        writeSampler.sampleNanos(System.nanoTime() - startForWrite);

        long startForRead = System.nanoTime();
        readData();
        readSampler.sampleNanos(System.nanoTime() - startForRead);

        jlbh.sample(System.nanoTime() - startTime);
    }

    private static void writeData() {
        REUSABLE_BYTES.clear();
        long now = System.nanoTime();
        long latency = System.nanoTime() - now;
//        System.out.println(latency);
        REUSABLE_BYTES.writeInt(123).writeInt(555555).writeLong(latency);
    }

    private static void readData() {
        int clientId = REUSABLE_BYTES.readInt();
        int point = REUSABLE_BYTES.readInt();
        long latency = REUSABLE_BYTES.readLong();
//        System.out.println("clientId: " + clientId);
//        System.out.println("point: " + point);
//        System.out.println("latency: " + latency);
    }
}
