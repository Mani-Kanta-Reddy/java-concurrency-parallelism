package multithreading.lifecycle;

public class BlockedState
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread t1 = new Thread(BlockedState::criticalSection);
        Thread t2 = new Thread(BlockedState::criticalSection);
        t1.setDaemon(true); // this thread completion doesn't halt program termination
        t2.setDaemon(true); // this thread completion doesn't halt program termination
        t1.start();
        // to make sure 't1' always starts first
        Thread.sleep(100);
        t2.start();
        // deliberately sleep main thread so that other threads start
        Thread.sleep(100);
        System.out.println(t2.getState());  //BLOCKED
    }

    private synchronized static void criticalSection() {
        while (true) {}
    }
}
