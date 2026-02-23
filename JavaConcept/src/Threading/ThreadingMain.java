package Threading;

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
    public static void main(String[] args) {
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
        System.out.println("Thread running:"+ Thread.currentThread().getName());
    }
}
