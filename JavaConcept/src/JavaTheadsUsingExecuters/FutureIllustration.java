package JavaTheadsUsingExecuters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureIllustration {
    public static void futureIll(){
        System.out.println("Illustration of the future "+Thread.currentThread().getName());
        ExecutorService executorService= Executors.newFixedThreadPool(3);
        List<Future<String>> future = new ArrayList<>();
        for(int i=0;i<6;i++){
            int taskId = i;
            Future<String> submit = executorService.submit(() -> {
                System.out.println("Submitted new task "+ taskId + " is running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "Task "+taskId+" is completed";
            });
            future.add(submit);
        }
        System.out.println("out of the first loop");
        for (Future<String> stringFuture : future) {
            try {
                String result = stringFuture.get();
                System.out.println(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Main Thread! Due to future.get()");
        executorService.shutdown();
    }
    public static void completableFutureIll(){
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int taskId = i;

            CompletableFuture<String> future =
                    CompletableFuture.supplyAsync(() -> {
                        System.out.println("Task " + taskId + " running on " + Thread.currentThread().getName());
                        try { Thread.sleep(3000); } catch (Exception e) {}
                        return "Task " + taskId + " completed";
                    }, executor);

            futures.add(future);
        }
        futures.forEach(f ->
                f.thenAccept(System.out::println)
        );

        executor.shutdown();
    }
    public static void main(String [] args) {
        completableFutureIll();
        futureIll();  //Does not go to line 66 "Main Thread" coz future.get() is there
        System.out.println("Main Thread!");

    }
}
