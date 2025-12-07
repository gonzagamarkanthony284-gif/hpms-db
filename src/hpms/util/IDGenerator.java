package hpms.util;

public class IDGenerator {
    public static String nextId(String type) {
        if ("P".equals(type)) return "P" + DataStore.pCounter.incrementAndGet();
        if ("S".equals(type)) return "S" + DataStore.sCounter.incrementAndGet();
        if ("A".equals(type)) return "A" + DataStore.aCounter.incrementAndGet();
        if ("B".equals(type)) return "B" + DataStore.bCounter.incrementAndGet();
        if ("R".equals(type)) return "R" + DataStore.rCounter.incrementAndGet();
        return java.util.UUID.randomUUID().toString();
    }
}

