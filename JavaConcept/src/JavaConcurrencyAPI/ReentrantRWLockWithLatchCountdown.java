package JavaConcurrencyAPI;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantRWLockWithLatchCountdown {

    public static int data=10;
    static ReentrantReadWriteLock reentrantLockReadWrite= new ReentrantReadWriteLock ();
    static ReentrantReadWriteLock.ReadLock readLock = reentrantLockReadWrite.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = reentrantLockReadWrite.writeLock();
    public static void readData(String name){
        readLock.lock();
        try {
            System.out.println("[ "+name+ " ] "+"Got the readings :" + data);
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            readLock.unlock();
            System.out.println("Released the read lock!");

        }
    }
    public static void writeData(int value, CountDownLatch latch){
        writeLock.lock();
        try {
            System.out.println("writing the data :" + value);
            data = value;
            Thread.sleep(4000);
            System.out.println("Wrote the data....data = " + data);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            writeLock.unlock();
            System.out.println("Released the write lock!");
            if(latch!=null){
                latch.countDown();
            }
        }
    }

    public static void main(String [] args){
        int firstBatch=1;
        CountDownLatch latch= new CountDownLatch(firstBatch);
        /*
        for(int i=0;i<=firstBatch;i++) {
            final int readerId = i;
            new Thread(() -> readData("Reader-" + readerId,latch)).start();
        }
        new Thread(()-> {
            try {
                latch.await();
                System.out.println("Done waiting for read threads!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            writeData(90);
        }).start();
        System.out.println("written the data............");
        for(int i=101;i<=200;i++) {
            final int readerId = i;
            new Thread(() -> readData("Reader-" + readerId,latch)).start();
        }
        */
        /* MADE THE WRITE THREAD TO RUN FIRST BY THE USE OF COUNTDOWNLATCH */

        for(int i=0;i<=firstBatch;i++) {
            final int readerId = i;
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                readData("Reader-" + readerId);
            }).start();
        }
        new Thread(()-> {
            writeData(90,latch);
        }).start();
        System.out.println("written the data............");


    }
}
