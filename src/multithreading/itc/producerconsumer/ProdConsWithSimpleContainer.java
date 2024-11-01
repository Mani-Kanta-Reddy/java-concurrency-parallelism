package multithreading.itc.producerconsumer;

import static multithreading.util.SleepAndWaitUtility.nap;
import static multithreading.util.SleepAndWaitUtility.sitTigh;

/*
Program to demonstrate  inter-thread communication with simple container
1. naive (busy waiting) -> bad
2. smart (using wait & notify thread semantics) -> good
 */
public class ProdConsWithSimpleContainer
{
    public static void main(String[] args)
    {
        SimpleContainer container = new SimpleContainer();
        // 1. naive (busy waiting) -> bad
        Thread naiveConsumer = new Thread(new NaiveConsumer(container));
        Thread naiveProducer = new Thread(new NaiveProducer(container));
        // 2. smart (using wait & notify thread semantics) -> good
        Thread smartConsumer = new Thread(new SmartConsumer(container));
        Thread smartProducer = new Thread(new SmartProducer(container));
        // naiveConsumer.start(); naiveProducer.start();
        smartConsumer.start(); smartProducer.start();
    }

}

class SimpleContainer {
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

class NaiveConsumer implements Runnable {
    private final SimpleContainer container;

    NaiveConsumer(SimpleContainer container)
    {
        this.container = container;
    }

    @Override
    public void run()
    {
        // this is call busy waiting which is bad
        while (container.isEmpty())
        {
            System.out.println("[consumer] is actively waiting for the producer to produce the item...");
        }
        System.out.println("[consumer] consumed item : " + container.get());
    }
}

class NaiveProducer implements Runnable {
    private final SimpleContainer container;

    NaiveProducer(SimpleContainer container)
    {
        this.container = container;
    }

    @Override
    public void run()
    {
        System.out.println("[producer] hard at work...");
        nap(500);
        int producedItem = 42;
        System.out.println("[producer] produced item : " + producedItem);
        container.set(producedItem);
    }
}

class SmartConsumer implements Runnable {
    private final SimpleContainer container;

    SmartConsumer(SimpleContainer container)
    {
        this.container = container;
    }

    @Override
    public void run()
    {
        synchronized (container)
        {
            System.out.println("[consumer] waiting for the producer to produce the item...");
            // // to avoid spurious wake-ups (we've to consider the fact that the threads are woken up by os thread scheduler without other thread notifying)
            while (container.isEmpty())
            {
                sitTigh(container);
            }
            System.out.println("[consumer] consumed item : " + container.get());
        }
    }
}
class SmartProducer implements Runnable {
    private final SimpleContainer container;

    SmartProducer(SimpleContainer container)
    {
        this.container = container;
    }

    @Override
    public void run()
    {
        synchronized (container)
        {
            System.out.println("[producer] hard at work...");
            nap(400);
            int producedItem = 42;
            System.out.println("[producer] produced item : " + producedItem);
            container.set(producedItem);
            container.notify();
        }
    }
}
