package JavaConcurrencyAPI;

import java.util.concurrent.locks.ReentrantLock;

public class RentrantLock {
    public static int counter=0;
    public static void main(String [] args) throws InterruptedException {
        ReentrantLock lock= new ReentrantLock();
        Thread t1 = new Thread(()->{
            lock.lock();
            System.out.println("Lock acquired by thread t1");
            try{
                counter++;
            }
            finally {
              lock.unlock();
            }
        });
        Thread t2= new Thread(()->{
           lock.lock();
           System.out.println("Lock aquired by thread t2");
           try{
               counter++;
           }
           finally {
               lock.unlock();
           }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Hi lock operations done :"+counter);
    }
}
