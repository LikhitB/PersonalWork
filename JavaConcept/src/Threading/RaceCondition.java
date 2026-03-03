package Threading;
public class RaceCondition {
    static int counter = 0;
    static volatile boolean volatileEnabled = true;
    public static synchronized void increment() {
        counter++;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
//                counter++;
                  increment();

            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });

        Thread t3 = new Thread(() -> {
            while(volatileEnabled) {
                System.out.println("volatileEnabled is set to true");
                break;
            }
            System.out.println("volatileEnabled is set to false");
        });
        t1.start();
        t2.start();
        t3.start();
        volatileEnabled=false;
        t1.join();
        t2.join();
        t3.join();

        System.out.println("Final counter: " + counter);
    }
}
