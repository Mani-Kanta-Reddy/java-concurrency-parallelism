package multithreading.lifecycle;

public class NewBornState
{
    public static void main(String[] args)
    {
        Thread thread = new Thread(() -> {
            // logic that needs to be executed on a different thread goes here..
        });
        System.out.println(thread.getState());  //NEW
    }
}
