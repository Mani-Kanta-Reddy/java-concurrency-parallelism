package multithreading.racecondition;

public class RaceConditionIssue
{
    static int x = 0;
    public static void runInParallel() {
        Thread t1 = new Thread(() -> x = 1);
        Thread t2 = new Thread(() -> x = 2);
        t1.start();
        t2.start();
        System.out.println(x);  //Output is not predicted may be 0 or 1 or 2 due to raceCondition
    }

    public static void buy(BankAccount bankAccount, String thing, int price) {
        bankAccount.amount -= price;
    }

    public static void buySafe(BankAccount bankAccount, String thing, int price) {
        synchronized (bankAccount)
        {
            bankAccount.amount -= price;
        }
    }

    public static void bankingProblem() throws InterruptedException
    {
        for(int i = 0; i < 10_000; i++) {
            BankAccount account = new BankAccount(50_000);
            Thread t1 = new Thread(() -> buy(account, "shoe", 3000));
            Thread t2 = new Thread(() -> buy(account, "Iphone", 4000));
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            if (account.amount != 37_000)
            {
                System.out.println("AHA! I've broken the bank account : " + account.amount);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException
    {

//        runInParallel();
        bankingProblem();
    }

    static class BankAccount {
        int amount;
        BankAccount(int amount) {
            this.amount = amount;
        }
    }
}
