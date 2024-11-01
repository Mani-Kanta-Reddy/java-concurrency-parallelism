package multithreading.util;

import lombok.SneakyThrows;

public final class SleepAndWaitUtility
{
    private SleepAndWaitUtility() { }
    @SneakyThrows
    public static void nap(long millis) {
        Thread.sleep(millis);
    }

    @SneakyThrows
    public static void sitTigh(Object object) {
        object.wait();
    }

    @SneakyThrows
    public static void sitTight(Object object, long millis) {
        object.wait(millis);
    }
}
