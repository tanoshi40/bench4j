package tanoshi.utils.timer;

public class Timer {

    public static long getNanoTime() {
        return System.nanoTime();
    }

    public static long getElapsedNanos(long fromTime) {
        return getNanoTime() - fromTime;
    }

}