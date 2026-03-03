package Threading;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

//Extending the Thread class
class ThreadExtends extends Thread{
    @Override
    public void run() {
        System.out.println("ExtendThread is running:"+Thread.currentThread().getName());
    }
}
//Implementing the Runnable interface
class ThreadImplements implements Runnable{
    @Override
    public void run() {
        System.out.println("ImplementThread is running:"+Thread.currentThread().getName());
    }
}
public class ThreadingMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadExtends t1 = new ThreadExtends();
        t1.start();

        ThreadImplements t2 = new ThreadImplements();
        Thread thread = new Thread(t2);
        thread.start();
//        thread.join();

        //Runnable with lambda function
        Thread threadLambda = new Thread(() -> {
            System.out.println("Lambda Thread is running:"+Thread.currentThread().getName());
        });
        threadLambda.start();

        //Using Callable with FutureTask
        Callable<Integer> callthreads= () -> {
            System.out.println("Callable Thread is running:"+Thread.currentThread().getName());
            return 42;
        };
        //FutureTask is a wrapper that lets you run a Callable in a thread and retrieve the result later. The get() method blocks the main thread until the result is available.
        FutureTask<Integer> futureTask = new FutureTask<>(callthreads);
        Thread callthread= new Thread(futureTask);
        callthread.start();
//        callthread.join();
        System.out.println("Result from Callable: " + futureTask.get());
        System.out.println("Main Thread running:"+ Thread.currentThread().getName());
    }
}
