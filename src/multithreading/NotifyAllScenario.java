package multithreading;

import lombok.SneakyThrows;

import java.util.stream.IntStream;

public class NotifyAllScenario
{
    public static void main(String[] args) {
        testNotifyAll();
    }

    @SneakyThrows
    private static void snooze(long millis) {
        Thread.sleep(millis);
    }

    @SneakyThrows
    private static void waite(Object obj) {
        obj.wait();
    }

    private static void testNotifyAll()
    {
        Object bell = new Object();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            new Thread(() -> {
                synchronized (bell)
                {
                    System.out.println("[student : " + i + "] waiting...");
                    waite(bell);
                    System.out.println("[student : " + i + "] hurray...");
                }

            }).start();
        });

        new Thread(() -> {
            snooze(2000);
            synchronized (bell)
            {
                System.out.println("[announcer] Rock'n roll" );
                bell.notifyAll();
            }
        }).start();
    }
}
