package JavaConcurrencyAPI;

import java.util.concurrent.Semaphore;

public class SemaphoreWithReentrantLock {
    static int counter = 0;
    public static void main(String[] args) {
        // Semaphore with 2 permits (only 2 threads can access critical section at once)
        Semaphore semaphore = new Semaphore(2);
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            try {
                System.out.println(threadName + " is waiting for a permit...");
                semaphore.acquire();
                System.out.println(threadName + " acquired a permit and is executing the critical section!");
                counter++;
                Thread.sleep(1000); // Simulate work
                System.out.println(threadName + " is leaving the critical section.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
                System.out.println(threadName + " released a permit.");
            }
        };
        // Start 5 threads
        for (int i = 1; i <= 5; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}
