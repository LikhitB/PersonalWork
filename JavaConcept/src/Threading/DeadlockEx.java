package Threading;

public class DeadlockEx {
    public static boolean notify=true;
    public static void main(String [] args ){
        final Object lock1 = new Object();
        final Object lock2 = new Object();
        Thread t1= new Thread(()->{
            synchronized (lock1){
                System.out.println("Acquired lock1, waiting for lock2");

                synchronized (lock2){
                    System.out.println("Acquired lock2");
                }

            }
        });

        Thread t2 = new Thread(()->{
            synchronized (lock1){
                System.out.println("Acquired lock2 waiting for lock1");
                lock1.notify();
                synchronized (lock2){
                    System.out.println("Acquired lock1 and lock2 ");
                }
            }
        });
        t1.start();
        t2.start();
    }
}
