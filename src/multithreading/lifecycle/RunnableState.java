package multithreading.lifecycle;

public class RunnableState
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread thread = new Thread(() -> {
            //logic that needs to be executed on a different thread goes here..
            while(true){}
        });
        thread.setDaemon(true); // this thread completion doesn't halt program termination
        thread.start();
        // deliberately sleep main thread so that other starts
        Thread.sleep(100);
        System.out.println(thread.getState());  //RUNNABLE
    }
}
