package tasks;

import net.openhft.chronicle.core.util.NanoSampler;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHTask;

import java.nio.ByteBuffer;

/**
 * @author lzn
 * @date 2023/10/01 17:13
 * @description
 */
public class RawBytesReadWriteTask implements JLBHTask {

    private byte[] rawBytes;
    private JLBH jlbh;
    private NanoSampler readSampler;
    private NanoSampler writeSampler;

    @Override
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        readSampler = jlbh.addProbe("Read Bytes");
        writeSampler = jlbh.addProbe("WriteBytes Bytes");
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

    private void writeData() {
        long nano = System.nanoTime();
        byte[] clientId = longToBytes(123, 4);
        byte[] type = longToBytes(0, 1);
        long now = System.nanoTime() - nano;
//        System.out.println(now);
        byte[] latency = longToBytes(now, 4);
        rawBytes = concatArrays(clientId, type, latency);
    }

    private void readData() {
        int part1 = ByteBuffer.wrap(rawBytes, 0, 4).getInt();
        byte part2 = ByteBuffer.wrap(rawBytes, 4, 1).get();
        long part3 = ByteBuffer.wrap(rawBytes, 5, 4).getInt();
    }

    private static byte[] longToBytes(long value, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[length - 1 - i] = (byte) (value >> i * 8);
        }
        return bytes;
    }

    public static byte[] concatArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
