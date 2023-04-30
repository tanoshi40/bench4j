package tanoshi.gcMemTesting;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class GCMemoryTesting {
    public static void testGCStats() {
        // test1();
        // test2();
        test3();
    }

    private static void test1() {
        printGCStats();

        Integer counter = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            counter += 1;
        }

        printGCStats();
        doSomething();

        printGCStats();
        doSomething();

        printGCStats();
    }

    public static void test2() {
        ProductManager manager = new ProductManager();

        for (int i = 0; i < 10; i++) {
            printGCStats();
            doSomething2(manager, 10000000);
        }
    }

    public static void test3() {
        MemoryTesting.perform();
    }

    private static void doSomething() {
        for (int i = 0; i < 100000000; i++) {
            Object object = new Object();
            String str = object.toString();
        }
    }

    private static void doSomething2(ProductManager manager, int amount) {
        manager.createObjects(amount);
    }

    private static void printGCStats() {
        long totalGarbageCollections = 0;
        long garbageCollectionTime = 0;

        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            long count = gc.getCollectionCount();

            if (count >= 0) {
                totalGarbageCollections += count;
            }

            long time = gc.getCollectionTime();

            if (time >= 0) {
                garbageCollectionTime += time;
            }
        }

        System.out.println("Total Garbage Collections: "
                + totalGarbageCollections);
        System.out.println("Total Garbage Collection Time (ms): "
                + garbageCollectionTime);

        System.out.printf("MEMORY: %s(TOT)\t| %s(MAX)\t| %s(FREE)\n", Runtime.getRuntime().totalMemory(), Runtime.getRuntime().maxMemory(), Runtime.getRuntime().freeMemory());

        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUse = memBean.getHeapMemoryUsage();

        System.out.printf("MEMORY: %s(COM)\t| %s(MAX)\t| %s(USE)\t| %s(INIT)\n", heapUse.getCommitted(), heapUse.getMax(), heapUse.getUsed(), heapUse.getInit());

        // System.out.println(heapUse.getMax()); // max memory allowed for jvm -Xmx flag (-1 if isn't specified)
        // System.out.println(heapUse.getCommitted()); // given memory to JVM by OS ( may fail to reach getMax, if there isn't more memory)
        // System.out.println(heapUse.getUsed()); // used now by your heap
        // System.out.println(heapUse.getInit()); // -Xms flag

        // |------------------ max ------------------------| allowed to be occupied by you from OS (less than xmX due to empty survival space)
        // |------------------ committed -------|          | now taken from OS
        // |------------------ used --|                    | used by your heap

        // memoryMxBean.getHeapMemoryUsage().getUsed()      <=> runtime.totalMemory() - runtime.freeMemory()
        // memoryMxBean.getHeapMemoryUsage().getCommitted() <=> runtime.totalMemory()
        // memoryMxBean.getHeapMemoryUsage().getMax()       <=> runtime.maxMemory()

        // INIT - represents the initial amount of memory (in bytes) that the Java virtual machine requests from the
        // operating system for memory management during startup. The Java virtual machine may request additional memory
        // from the operating system and may also release memory to the system over time. The value of init may be
        // undefined.
        //
        // USED - represents the amount of memory currently used (in bytes).
        //
        // COMMITTED - represents the amount of memory (in bytes) that is guaranteed to be available for use by the
        // Java virtual machine. The amount of committed memory may change over time (increase or decrease). The Java
        // virtual machine may release memory to the system and committed could be less than init. committed will always
        // be greater than or equal to used.
        //
        // MAX - represents the maximum amount of memory (in bytes) that can be used for memory management. Its value
        // may be undefined. The maximum amount of memory may change over time if defined. The amount of used and
        // committed memory will always be less than or equal to max if max is defined. A memory allocation may fail
        // if it attempts to increase the used memory such that used > committed even if used <= max would still be
        // true (for example, when the system is low on virtual memory).
        //
        // https://docs.oracle.com/javase/9/docs/api/java/lang/management/MemoryUsage.html

        System.out.println("\n--------\n");
    }
}

