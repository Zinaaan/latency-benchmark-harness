package tasks.maps;

import net.openhft.chronicle.core.util.NanoSampler;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHTask;

import java.util.HashMap;
import java.util.Random;

/**
 * @author lzn
 * @date 2023/10/01 22:47
 * @description
 */
public class JDKCounterMapReadWriteTask implements JLBHTask {

    private JLBH jlbh;
    private NanoSampler readSampler;
    private NanoSampler writeSampler;
    private final Random random = new Random();

    private final HashMap<Integer, Integer> map = new HashMap<>(50000);

    @Override
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        readSampler = jlbh.addProbe("JDK Read Map");
        writeSampler = jlbh.addProbe("JDK Write Map");
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
        map.get(random.nextInt(10000));
    }

    private void writeData() {
        map.put(random.nextInt(10000), map.getOrDefault(random.nextInt(10000), 0) + 1);
    }
}
