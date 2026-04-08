package JavaConcurrencyAPI;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantLockReadWrite {

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
    public static void writeData(int value){
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
        }
    }

    public static void main(String [] args){
        for(int i=0;i<=100;i++) {
            final int readerId = i;
            new Thread(() -> readData("Reader-" + readerId)).start();
        }
        new Thread(()->writeData(50));
    }
}
