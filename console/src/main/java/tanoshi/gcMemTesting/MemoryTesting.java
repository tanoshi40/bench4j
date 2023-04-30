package tanoshi.gcMemTesting;

import java.util.ArrayList;
import java.util.List;

public class MemoryTesting {

    private static final long MEGABYTE = 1024L * 1024L;

    public static double bytesToMegabytes(double bytes) {
        return bytes / MEGABYTE;
    }

    public static void perform() {
        System.out.println();
        doStuff(false);
        doStuff(false);
        doStuff(false);
        doStuff(false);
        System.out.println();
        doStuff(true);
        doStuff(true);
        doStuff(true);
        doStuff(true);
        System.out.println();
        doStuff(true);
        doStuff(false);
        doStuff(true);
        doStuff(false);
        System.out.println();
    }

    private static void doStuff(boolean doGC) {
        // Get the Java runtime and run the garbage collector
        Runtime runtime = Runtime.getRuntime();

        if (doGC) {
            runtime.gc();
        }
        long before = getUsageMemory(runtime);
        List<Person> list = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            list.add(new Person("Jim", "Knopf"));
        }

        // Calculate the used memory
        double userMemory = getUsageMemory(runtime) - before;
        System.out.printf("Used memory is: %.3fmb (%sb) (doGC:%b)%n", bytesToMegabytes(userMemory), userMemory, doGC);
    }
    private static long getUsageMemory(Runtime runtime) {
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
