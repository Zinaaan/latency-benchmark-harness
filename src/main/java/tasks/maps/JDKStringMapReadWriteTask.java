package tasks.maps;

import net.openhft.chronicle.core.util.NanoSampler;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHTask;
import org.agrona.collections.Object2ObjectHashMap;

import java.util.HashMap;

/**
 * @author lzn
 * @date 2023/10/01 23:00
 * @description
 */
public class JDKStringMapReadWriteTask implements JLBHTask {
    private JLBH jlbh;
    private NanoSampler readSampler;
    private NanoSampler writeSampler;

    private final HashMap<String, String> map = new HashMap<>(50000);

    @Override
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        readSampler = jlbh.addProbe("JDK String Read Map");
        writeSampler = jlbh.addProbe("JDK String Write Map");
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

    private void readData() {
        map.get(RandomStringGenerator.generateRandomString(20));
    }

    private void writeData() {
        map.put(RandomStringGenerator.generateRandomString(20), "123");
    }
}
