package tasks.maps;

import net.openhft.chronicle.core.util.NanoSampler;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lzn
 * @date 2023/11/05 14:00
 * @description
 */
public class JavaMapVsChronicleMap implements JLBHTask {

    // 1 million key-value pairs
    private static final int MAP_SIZE = 1000000;
    private JLBH jlbh;
    private Map<Integer, String> hashMap;
    private ChronicleMap chronicleMap;
    private NanoSampler hashMapSampler;
    private NanoSampler chronicleSampler;

    public void hashMapGet() {
        // Retrieve a value from HashMap
        int randomKey = (int) (Math.random() * MAP_SIZE);
        hashMap.get(randomKey);
    }

    public void chronicleMapGet() {
        // Retrieve a value from Chronicle Map
        int randomKey = (int) (Math.random() * MAP_SIZE);
        chronicleMap.get(randomKey);
    }

    public JLBHOptions generateOptions() {
        return new JLBHOptions()
                .warmUpIterations(10000)
                .iterations(10000)
                .runs(3)
                .throughput(50000)
                .recordOSJitter(true)
                .accountForCoordinatedOmission(true);
    }

    @Override
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        hashMapSampler = jlbh.addProbe("HashMap");
        chronicleSampler = jlbh.addProbe("ChronicleMap");
        // Initialize HashMap
        hashMap = new HashMap<>();
        for (int i = 0; i < MAP_SIZE; i++) {
            hashMap.put(i, "value" + i);
        }

        // Initialize Chronicle Map
        chronicleMap = ChronicleMapBuilder.of(Integer.class, String.class)
                .name("test-map")
                .entries(MAP_SIZE)
                // Average value size for efficient memory allocation
                .averageValueSize("value1000".length())
                .create();
        for (int i = 0; i < MAP_SIZE; i++) {
            chronicleMap.put(i, "value" + i);
        }

        System.out.println("HashMap size: " + hashMap.size());
        System.out.println("Chronicle size: " + chronicleMap.size());
    }

    @Override
    public void run(long startTimeInNanos) {
        long startForHashMap = System.nanoTime();
        hashMapGet();
        hashMapSampler.sampleNanos(System.nanoTime() - startForHashMap);

        long startForChronicleMap = System.nanoTime();
        chronicleMapGet();
        chronicleSampler.sampleNanos(System.nanoTime() - startForChronicleMap);

        jlbh.sample(System.nanoTime() - startTimeInNanos);
    }
}