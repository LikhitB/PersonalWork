package JavaConcurrencyAPI;

import java.util.concurrent.locks.StampedLock;

public class StampLock {
    static int data=0;
    static  StampedLock stampedLock= new StampedLock();
    static void optimisticRead(){
        long stamp=stampedLock.tryOptimisticRead();
        if(stampedLock.validate(stamp)){
            System.out.println("[Optimistic read] reading data i.e:"+ data);
        }
        else {
            stamp = stampedLock.readLock();
            try {
                System.out.println("Some writing is going on so stamp is acquired!");
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
    }
    static void write(int value) {
        long stamp = stampedLock.writeLock();
        try {
            data = value;
            System.out.println("[Write] Written: " + value);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    public static void main(String [] args){
        new Thread(StampLock::optimisticRead).start();
        new Thread(()-> write(30)).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(()-> write(50)).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();
        new Thread(StampLock::optimisticRead).start();






    }
}
