package multithreading.visibility;
public class VisibilitySolutionInMultiThreading {
    volatile static boolean flag = true;
    public static void main(String[] args) throws InterruptedException{
        Thread t1 = new Thread(() -> {
            System.out.println("Started T1");
            while(flag) {
                //do-something
            }
            System.out.println("END T1");
        });
        t1.start();
        //deliberately delay below update and let t1 start first and process
        Thread.sleep(300);
        flag = false; //main-thread changes the flag to false so that t1 can be saved from infinite
        t1.join(); // make main-thread wait until t1 finishes its execution
        /* We would except that this pgm ends and prints the below line :) and this time is does
           print the below and terminates successfully b/c T1 is made to always fetch the value of flag
           from either shared cache or main memory (Ram) but not from its own cache
        */
        System.out.println("END");
    }
}
