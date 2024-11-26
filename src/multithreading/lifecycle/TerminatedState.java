package multithreading.lifecycle;

public class TerminatedState
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread thread = new Thread(() -> {
            // logic that needs to be executed on a different thread goes here..
        });
        thread.start();
        thread.join();  // main thread waits until other thread completes
        System.out.println(thread.getState());  //TERMINATED
    }
}
