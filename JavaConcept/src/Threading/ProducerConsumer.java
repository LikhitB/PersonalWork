package Threading;
public class ProducerConsumer {
    public static int data=0;
    public static boolean notify=false;
    public static void main(String[] args) throws InterruptedException{
        final Object lock = new Object();
        Thread consumer = new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+" Thread is running:");
            synchronized (lock) {
                while (!notify) {
                    try {
                        System.out.println("waiting for the producer to produce....");
                        lock.wait();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            System.out.println("Got the data from the producer... :"+data);
            notify=false;
        });


        Thread producer = new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+" Thread is running:");
            synchronized (lock) {
                System.out.println("producing the data for the consumer.....");
                try {
                    Thread.sleep(5000);
                    notify=true;
                    data = 50;
                    lock.notify();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("produced data to the consumer");
        });


        producer.start();
        consumer.start();
        System.out.println(Thread.currentThread().getName()+" Thread is running:");
    }

}
