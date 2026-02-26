package Threading;

<<<<<<< HEAD
=======
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

>>>>>>> 8a418f4 (d)
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
<<<<<<< HEAD
    public static void main(String[] args) {
=======
    public static void main(String[] args) throws ExecutionException, InterruptedException {
>>>>>>> 8a418f4 (d)
        ThreadExtends t1 = new ThreadExtends();
        t1.start();

        ThreadImplements t2 = new ThreadImplements();
        Thread thread = new Thread(t2);
        thread.start();

        //Runnable with lambda function
        Thread threadLambda = new Thread(() -> {
            System.out.println("Lambda Thread is running:"+Thread.currentThread().getName());
        });
        threadLambda.start();
<<<<<<< HEAD
=======

        //Using Callable with FutureTask
        Callable<Integer> callthreads= () -> {
            System.out.println("Callable Thread is running:"+Thread.currentThread().getName());
            return 42;
        };
        //FutureTask is a wrapper that lets you run a Callable in a thread and retrieve the result later. The get() method blocks the main thread until the result is available.
        FutureTask<Integer> futureTask = new FutureTask<>(callthreads);
        Thread callthread= new Thread(futureTask);
        callthread.start();
        System.out.println("Result from Callable: " + futureTask.get());

>>>>>>> 8a418f4 (d)
        System.out.println("Thread running:"+ Thread.currentThread().getName());
    }
}
