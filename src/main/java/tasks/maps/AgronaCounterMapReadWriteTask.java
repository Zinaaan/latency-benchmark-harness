package tasks.maps;

import net.openhft.chronicle.core.util.NanoSampler;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHTask;
import org.agrona.collections.Int2IntCounterMap;

import java.util.Random;

/**
 * @author lzn
 * @date 2023/10/01 22:47
 * @description
 */
public class AgronaCounterMapReadWriteTask implements JLBHTask {

    private JLBH jlbh;
    private NanoSampler readSampler;
    private NanoSampler writeSampler;
    private final Random random = new Random();
    private final Int2IntCounterMap map = new Int2IntCounterMap(0);

    @Override
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        readSampler = jlbh.addProbe("Agrona Read Map");
        writeSampler = jlbh.addProbe("Agrona Write Map");
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
        map.incrementAndGet(random.nextInt(10000));
    }
}
