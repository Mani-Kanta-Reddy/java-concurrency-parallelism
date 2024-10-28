package multithreading.racecondition;

public class CounterProblemInMultiThreading {
    static int counter = 0;
    static final int limit = 1_00_000; //1Lakh
    public static void main(String[] args) throws InterruptedException
    {
        //create thread
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < limit; i++) {
                counter++;
            }
        });
        //create another thread
        Thread t2 = new Thread(() -> {
            for(int i = 0; i < limit; i++) {
                counter++;
            }
        });
        //start the threads
        t1.start();
        t2.start();
        //make main thread to wait until t1 & t2 finishe their tasks
        t1.join();
        t2.join();
        //finally print the counter, we expect it to be 2,00,000 (2 lakhs) 
        System.out.println(counter);
        /*
        but this ^^ always prints the result which is always < 2,00,000 due to race-conditions
        Solution to this problem is to lock the concurrent access on the shared resource (var, obj)
        And this can be achieved in many ways:
        1. Using synchronized blocks
        2. If the shared Resources are simple counters use Atomics
        3. Using Semaphores
        4. Using Locks
        */
    }
}
