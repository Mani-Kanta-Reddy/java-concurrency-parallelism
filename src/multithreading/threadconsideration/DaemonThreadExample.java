package multithreading.threadconsideration;

public class DaemonThreadExample {
    public static void main(String[] args) {
        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("Daemon thread running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // ignore this for now, we'll discuss this in our upcoming posts
                }
            }
        });

        daemonThread.setDaemon(true); // setDaemon property
        daemonThread.start();

        System.out.println("Main thread ending..."); // JVM exits as soon as the main thread completes
    }
}
