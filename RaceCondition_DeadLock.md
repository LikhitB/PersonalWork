## Volatile , Synchronized threading
- Each thread can cache variables in CPU registers or L1/L2 cache. T1 may be reading its own cached copy of `lock` variable and never see the update that main thread wrote to main memory.
- `volatile` tells the JVM — never cache this variable. Always read from and write to main memory directly. Every thread sees the latest value immediately.
- `Synchronised` keyword ensures the threads are locked and are running in synchrounous way.
## Example 

***In Java, multiple threads can run at the same time.***
Imagine 3 people trying to update the same bank account balance.

***Example:***
Balance = 100
Thread-1 wants to withdraw 10
Thread-2 wants to withdraw 20
Both run at the same time.
What could happen
Step by step:

Thread1 reads balance → 100
Thread2 reads balance → 100

Now both think balance is 100.

Then:
Thread1 writes → 90
Thread2 writes → 80

Final balance becomes 80 ❌
But correct result should be:
100 - 10 - 20 = 70

***This problem is called a race condition.***

## We use `synchronized` when:
1. Multiple threads
2. Access the same shared data
3. And modify it
4. ***This is called critical section.***

### ***A thread keeps the first lock until the synchronized block finishes.***

### Example:

``` java
synchronized(lock1){
   while(true){// still lock1 is held by the thread}
      // do some work
   }
}
## until it completes the block, it holds lock1. No other thread can acquire lock1 until the first thread releases it.This concept is very important in java***
```






