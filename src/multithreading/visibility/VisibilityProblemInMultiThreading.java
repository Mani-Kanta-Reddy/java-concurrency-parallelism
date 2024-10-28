package multithreading.visibility;
public class VisibilityProblemInMultiThreading {
    static boolean flag = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("Started T1:");
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
        // We would except that this pgm ends and prints the below line :) run this pgm and see :(
        System.out.println("END");

        /*
        Result for the above program is always stuck in infinite state the reason
        is the update made by main-thread to `flag` is never read by `t1` (since it's always reading
        from it's own cache)
        
        The Solution to this problem is make `t1` to always read from shared cache or Ram
        (We can impose this kinda behaviour to variables by declaring them as `volatile`)
        */
    }
}
