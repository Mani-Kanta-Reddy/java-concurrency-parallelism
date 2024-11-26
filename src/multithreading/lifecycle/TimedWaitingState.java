package multithreading.lifecycle;

public class TimedWaitingState
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread thread = new Thread(() -> {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true); // this thread completion doesn't halt program termination
        thread.start();
        Thread.sleep(100);
        System.out.println(thread.getState());  //TIMED_WAITING
    }
}
