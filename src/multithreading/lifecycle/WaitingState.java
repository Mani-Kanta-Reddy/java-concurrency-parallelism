package multithreading.lifecycle;

public class WaitingState
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread t1 = new Thread(() -> {
            Thread t2 = new Thread(() -> {
                while(true) {}
            });
            t2.start();
            try
            {
                t2.join();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        });
        t1.setDaemon(true); // this thread completion doesn't halt program termination
        t1.start();
        // deliberately sleep main thread so that other thread starts
        Thread.sleep(100);
        System.out.println(t1.getState());  //WAITING
    }
}
