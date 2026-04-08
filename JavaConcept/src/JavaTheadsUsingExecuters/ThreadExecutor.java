package JavaTheadsUsingExecuters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor {
    public static void main(String [] arg) throws InterruptedException {
        System.out.println("Illustration of the executorService "+Thread.currentThread().getName());

        /*

        Only 1 thread — tasks run one at a time, in order
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> System.out.println("Task 1")); // runs first
        executor.submit(() -> System.out.println("Task 2")); // runs second
        executor.submit(() -> System.out.println("Task 3")); // runs third

        */

        //Executors.newFixedThreadPool(2) takes the number of threads prepares ThreadPoolExecutor and returns ThreadPoolExecutor , like factory takes a thing and produces something.
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for(int i = 0; i < 50; i++) {
            int taskId = i;  //Lambda takes only final variables inside and here taskId is effectively final because it is never changed in this code so lambda takes these kind of variables also.
            executorService.submit(() -> {
                System.out.println("Submitted new task "+ taskId + "is running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executorService.shutdown();
        System.out.println("All tasks have been finished "+Thread.currentThread().getName());
    }

}
