package multithreading.itc.producerconsumer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import static multithreading.util.SleepAndWaitUtility.nap;
import static multithreading.util.SleepAndWaitUtility.sitTigh;

/*
Program to demonstrate inter thread communication with large buffer
 */
public class ProdConsWithLargeBuffer
{
    public static void main(String[] args)
    {
        LargeBuffer<Integer> buffer = new LargeBuffer<>(10);
        Thread consumer = new Thread(new Consumer<>(buffer, 0, new IntegerProcessor()));
        Thread producer = new Thread(new Producer<>(buffer, 0, new IntegerFactory()));
        consumer.start();
        producer.start();
    }
}

class LargeBuffer<T>
{
    private final int capacity;
    private final Queue<T> buffer;

    LargeBuffer(int capacity)
    {
        this.capacity = capacity;
        buffer = new ArrayDeque<>();
    }

    public int getCapacity()
    {
        return capacity;
    }

    public Queue<T> getBuffer()
    {
        return buffer;
    }

    public boolean isEmpty()
    {
        return buffer.isEmpty();
    }

    public T poll()
    {
        return buffer.poll();
    }

    public void offer(T item)
    {
        buffer.offer(item);
    }

    public boolean isFull()
    {
        return buffer.size() == capacity;
    }
}

class Consumer<T> implements Runnable
{
    private final LargeBuffer<T> buffer;
    private final int id;
    private final ItemProcessor<T> processor;

    Consumer(LargeBuffer<T> buffer, int id, ItemProcessor<T> processor)
    {
        this.buffer = buffer;
        this.id = id;
        this.processor = processor;
    }


    @Override
    public void run()
    {
        while (true)
        {
            T item;
            synchronized (buffer)
            {
                while (buffer.isEmpty())
                {
                    System.out.println("[consumer : " + id + "] " + "buffer is empty waiting for the producers to produce...");
                    sitTigh(buffer);
                }
                // now there is at least one item to consume from buffer
                item = buffer.poll();
                System.out.println("[consumer : " + id + "] " + "consumed item : " + item);
                buffer.notify();
            }
            processor.process(item);
        }
    }
}

class Producer<T> implements Runnable
{
    private final LargeBuffer<T> buffer;
    private final int id;
    private final ItemFactory<T> itemFactory;

    Producer(LargeBuffer<T> buffer, int id, ItemFactory<T> itemFactory)
    {
        this.buffer = buffer;
        this.id = id;
        this.itemFactory = itemFactory;
    }


    @Override
    public void run()
    {
        while (true)
        {
            T item = itemFactory.create();
            synchronized (buffer)
            {
                while (buffer.isFull())
                {
                    System.out.println("[producer : " + id + "] " + "buffer is full waiting for the consumers to consumer...");
                    sitTigh(buffer);
                }
                buffer.offer(item);
                System.out.println("[producer : " + id + "] " + "produced item : " + item);
                buffer.notify();
            }
        }
    }
}

interface ItemFactory<T>
{
    T create();
}

class IntegerFactory implements ItemFactory<Integer>
{
    private int value;
    private final Random sleepRandomizer = new Random();
    @Override
    public Integer create()
    {
        // processing simulation for the item production
        nap(sleepRandomizer.nextInt(500));
        return value++;
    }
}

interface ItemProcessor<T>
{
    void process(T item);
}

class IntegerProcessor implements ItemProcessor<Integer>
{
    private final Random random = new Random();

    @Override
    public void process(Integer item)
    {
        // actual process logic goes here...
        nap(random.nextInt(500));
    }
}
