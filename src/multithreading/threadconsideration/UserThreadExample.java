package multithreading.threadconsideration;

public class UserThreadExample {
    public static void main(String[] args) {
        Thread userThread = new Thread(() -> {
            System.out.println("User thread running...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // ignore this for now, we'll discuss this in our upcoming posts
            }
            System.out.println("User thread finished.");
        });

        userThread.start(); // JVM waits (~5SEC) for userThread completion
    }
}
