package tasks.bytes;

import net.openhft.chronicle.core.util.NanoSampler;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHTask;
import org.agrona.concurrent.UnsafeBuffer;

/**
 * @author lzn
 * @date 2023/10/01 17:19
 * @description
 */
public class AgronaBytesReadWriteTask implements JLBHTask {

    private final byte[] bufferBytes = new byte[16];
    private final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(bufferBytes);
    private JLBH jlbh;
    private NanoSampler readSampler;
    private NanoSampler writeSampler;

    @Override
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        readSampler = jlbh.addProbe("Agrona Read Bytes");
        writeSampler = jlbh.addProbe("Agrona Write Bytes");
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
        int par1 = unsafeBuffer.getInt(0);
        byte par2 = unsafeBuffer.getByte(4);
        long par3 = unsafeBuffer.getLong(5);
    }

    private void writeData() {
        long nano = System.nanoTime();
        unsafeBuffer.putInt(0, 123);
        unsafeBuffer.putByte(4, (byte) 0);
        unsafeBuffer.putLong(5, System.nanoTime() - nano);
    }
}
