package multithreading.racecondition;
import java.util.concurrent.atomic.AtomicInteger;

public class SolveCounterProblemWithAtomics {
    static AtomicInteger counter = new AtomicInteger(0);
    static final int limit = 1_00_000;  // 1 Lakh
    public static void main(String[] args) throws InterruptedException {
        //create a thread
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < limit; i++) {
                counter.getAndIncrement();
            }
        });
        //create another thread
        Thread t2 = new Thread(() -> {
            for(int i = 0; i < limit; i++) {
                counter.incrementAndGet();
            }
        });
        //start the threads
        t1.start();
        t2.start();
        //make main thread wait until t1 & t2 finishes
        t1.join();
        t2.join();
        /*
        finally print the counter. We expect it to be 2,00,000 & it'll be 2L how many ever times
        you run this program
        */
        System.out.println(counter);
    }
}
