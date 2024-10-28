package multithreading.racecondition;
public class SolveCounterProblemWithSynchronizedBlock {
    static int counter = 0;
    static final int limit = 1_00_000;
    static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException{
        Thread t1 = new Thread(() -> {
            synchronized(lock) {
                for(int i = 0; i < limit; i++) 
                    counter++;
            }
        });
        
        Thread t2 = new Thread(() -> {
            synchronized(lock) {
                for(int i = 0; i < limit; i++)
                    counter++;
            }
        });
        t1.setName("T1");
        t2.setName("T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //We expcet the counter to be 2,00,000 and it will be 2L how many times you run this pgm
        System.out.println(counter);
    }
}
