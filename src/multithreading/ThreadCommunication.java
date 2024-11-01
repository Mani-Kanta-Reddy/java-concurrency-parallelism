package multithreading;

import lombok.SneakyThrows;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.stream.IntStream;

public class ThreadCommunication
{
    static class SimpleContainer
    {
        int value;

        public int get()
        {
            int result = value;
            value = 0;
            return result;
        }

        public void set(int newValue)
        {
            value = newValue;
        }

        public boolean isEmpty()
        {
            return value == 0;
        }
    }

    @SneakyThrows
    private static void nap(long millis)
    {
        Thread.sleep(millis);
    }

    @SneakyThrows
    private static void sitTight(Object obj)
    {
        obj.wait();
    }

    public static void main(String[] args)
    {
        // naiveProdCons();
        // smartProdCons();
        // prodConsLargeBuffer();
//        multiProdCons(3, 3, 3);
//        testNotifyAll();
//        demoDeadLock();
        demoLiveLock();
    }

    private static void demoLiveLock()
    {
        Friend sam = new Friend("sam");
        Friend pierre = new Friend("pierre");
        new Thread(() -> sam.pass(pierre)).start();
        new Thread(() -> pierre.pass(sam)).start();
    }

    private static void demoDeadLock()
    {
        Friend sam = new Friend("sam");
        Friend pierre = new Friend("pierre");

        new Thread(() -> sam.bow(pierre)).start();
        new Thread(() -> pierre.bow(sam)).start();
    }

    private static void multiProdCons(int nConsumers, int nProducers, int capacity)
    {
        Queue<Integer> buffer = new ArrayDeque<>();
        IntStream.rangeClosed(1, nConsumers).forEach(i -> new Consumer(i, buffer).start());
        IntStream.rangeClosed(1, nProducers).forEach(i -> new Producer(i, capacity, buffer).start());
    }

    private static void prodConsLargeBuffer()
    {
        Queue<Integer> buffer = new ArrayDeque<>();
        final int CAP = 3;

        //create consumer
        Random consGen = new Random();
        Thread consumer = new Thread(() -> {
            while (true)
            {
                synchronized (buffer)
                {
                    if (buffer.isEmpty())
                    {
                        System.out.println("[consumer] buffer empty waiting...");
                        sitTight(buffer);
                    }
                    // either buffer is non-empty or consumer is woken up by producer
                    Integer itemReceived = buffer.poll();
                    System.out.println("[consumer] I've consumed : " + itemReceived);

                    buffer.notify();
                }
                nap(consGen.nextInt(500));
            }
        });

        //create producer
        Random prodsGen = new Random();
        Thread producer = new Thread(() -> {
            int i = 0;
            while (true)
            {
                synchronized (buffer)
                {
                    if (buffer.size() == CAP)
                    {
                        System.out.println("[producer] buffer full waiting...");
                        sitTight(buffer);
                    }
                    //either buffer has at-least one empty spot or producer woken up by consumer
                    System.out.println("[producer] I've produced : " + i);
                    buffer.offer(i++);

                    buffer.notify();
                }
                nap(prodsGen.nextInt(500));
            }
        });

        consumer.start();
        producer.start();
    }

    private static void smartProdCons()
    {
        SimpleContainer container = new SimpleContainer();

        //create consumer
        Thread consumer = new Thread(() -> {
            synchronized (container)
            {
                System.out.println("[consumer] waiting...");
                // to avoid spurious wake-ups (we've to consider the fact that the threads are woken up by os thread scheduler without other thread notifying)
                while(container.isEmpty()) {
                    sitTight(container);
                }
            }
            System.out.println("[consumer] I've consumed value : " + container.get());
        });

        //create producer
        Thread producer = new Thread(() -> {
            System.out.println("[producer] hard at work...");
            nap(1000);
            int result = 42;
            synchronized (container)
            {
                System.out.println("[producer] produced value : " + result);
                container.set(result);
                container.notify();
            }
        });

        consumer.start();
        producer.start();
    }

    private static void naiveProdCons()
    {
        SimpleContainer container = new SimpleContainer();
        //create consumer
        Thread consumer = new Thread(() -> {
            System.out.println("[consumer] is waiting...");
            while (container.isEmpty()) //busy waiting too bad
            {
                System.out.println("[consumer] is actively waiting...");
            }
            System.out.println("[consumer] I've consumed : " + container.get());
        });

        //create producer
        Thread producer = new Thread(() -> {
            System.out.println("[producer] hard at work...");
            nap(1000);
            int result = 42;
            System.out.println("[producer] produced value : " + result);
            container.set(result);
        });

        consumer.start();
        producer.start();
    }

    public static void testNotifyAll()
    {
        Object bell = new Object();
        //students
        IntStream.rangeClosed(1, 10).forEach(i -> {
            new Thread(() -> {
                synchronized (bell)
                {
                    System.out.println("[student : " + i + "] waiting...");
                    sitTight(bell);
                }
                System.out.println("[student : " + i + "] hurray!");
            }).start();
        });

        //pune (announcer)
        new Thread(() -> {
            nap(2000);
            synchronized (bell)
            {
                System.out.println("[announcer] bell has rung");
                bell.notifyAll();
            }
        }).start();
    }

    private static class Consumer extends Thread
    {
        final int id;
        final Queue<Integer> buffer;

        public Consumer(int id, Queue<Integer> buffer)
        {
            this.id = id;
            this.buffer = buffer;
        }

        @Override
        public void run()
        {
            final Random consGen = new Random();
            while (true)
            {
                synchronized (buffer)
                {
                    while (buffer.isEmpty())
                    {
                        System.out.println("[consumer " + id + "] " + "buffer empty waiting...");
                        sitTight(buffer);
                    }
                    //buffer is non-empty and consumer notified
                    Integer result = buffer.poll();
                    System.out.println("[consumer " + id + "] " + "consumed " + result);
                    buffer.notify();    //notifies one of the consumer or producer
                }
                nap(consGen.nextInt(500));
            }
        }
    }
    private static class Producer extends Thread
    {
        final int capacity;
        final int id;
        final Queue<Integer> buffer;

        public Producer(int id, int capacity, Queue<Integer> buffer)
        {
            this.id = id;
            this.capacity = capacity;
            this.buffer = buffer;
        }

        @Override
        public void run()
        {
            final Random prodsGen = new Random();
            int i = 0;
            while (true)
            {
                synchronized (buffer)
                {
                    while (buffer.size() == capacity)
                    {
                        System.out.println("[producer " + id + "] " + "buffer full waiting...");
                        sitTight(buffer);
                    }
                    //buffer has at least one empty spot and consumer notified
                    System.out.println("[producer " + id + "] " + "produced " + i);
                    buffer.offer(i++);
                    buffer.notify(); //notifies one of the consumer of producer
                }
                nap(prodsGen.nextInt(500));
            }
        }
    }

    private static class Friend
    {
        private final String name;
        private String side = "right";

        public Friend(String name)
        {
            this.name = name;
        }

        public void bow(Friend other)
        {
            synchronized (this)
            {
                System.out.println(this + " bowing to my friend " + other);
                other.rise(this);
                System.out.println(this + " my friend " + other + " has risen");
            }
        }

        private void rise(Friend other)
        {
            synchronized (this)
            {
                System.out.println(this + " rising to my friend " + other);
            }
        }

        @Override
        public String toString()
        {
            return "'" + name + "'";
        }

        public void pass(Friend other)
        {
            while(this.side == other.side) {
                System.out.println(this + " oh, but please " + other + " feel free to pass");
                switchSide();
                nap(1000);
            }
        }

        private void switchSide()
        {
            if(this.side.equals("right")) this.side = "left";
            else this.side = "right";
        }
    }
}
