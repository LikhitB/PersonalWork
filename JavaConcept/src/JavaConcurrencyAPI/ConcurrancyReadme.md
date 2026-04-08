# Java Concurrency Locks — Quick Notes

---

## 1. ReentrantLock

`ReentrantLock` is an explicit lock from `java.util.concurrent.locks` used to control access to shared resources between threads.

### Why Use It?
- Provides more control than `synchronized`
- Manual lock and unlock
- Fair locking option
- Ability to try acquiring a lock
- Interruptible locking

### Basic Usage
```text
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

> **Why `finally` is important:**
> If an exception occurs and the lock is not released, other threads will wait forever.

---

## 2. `join()` — Waiting for Threads

`join()` ensures that the main thread waits until another thread finishes execution.

**Example:**
```text
t1.start();
t2.start();

t1.join();
t2.join();
```

- **Without `join()`:** Main thread may print results **before worker threads finish**.
- **With `join()`:** Main thread waits for both threads, ensuring correct final output.

---

## 3. ReentrantReadWriteLock

`ReentrantReadWriteLock` separates locking into two types:

| Lock       | Purpose                                  |
| ---------- | ---------------------------------------- |
| Read Lock  | Multiple threads can read simultaneously |
| Write Lock | Only one thread can modify data          |

This improves performance in **read-heavy systems**.

> Multiple threads can read the data when no write is happening.
> May lead to starvation if writers or readers are blocked for too long.

### Read Lock Example
```text
readLock.lock();
try {
    // read shared data
} finally {
    readLock.unlock();
}
```

### Write Lock Example
```text
writeLock.lock();
try {
    // modify shared data
} finally {
    writeLock.unlock();
}
```

---

## Lock Behavior Summary

```
Readers + Readers  → Allowed
Readers + Writer   → Blocked
Writer + Writer    → Blocked
```

---

## When to Use ReadWriteLock

Best suited when:
```
reads >> writes
```

**Common scenarios:**
- Caches
- Configuration stores
- In-memory data structures

---

## Simple Mental Model

Think of a **library**:
- Many students can **read books at the same time**.
- When the librarian **updates the catalog**, everyone must wait.

---

## Quick Takeaway

```
ReentrantLock   → exclusive thread control
join()          → wait for thread completion
ReadWriteLock   → multiple readers, single writer
```

---

## 4. CountDownLatch

`CountDownLatch` is a synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes.

- No matter the order you start the threads, a thread calling `await()` will wait until the latch count reaches zero.

### Basic Usage
```text
CountDownLatch latch = new CountDownLatch(N); // N = number of threads to wait for
for (int i = 0; i < N; i++) {
    new Thread(() -> {
        // do work
        latch.countDown(); // signal completion
    }).start();
}
latch.await(); // main thread waits here until all N threads call countDown()
```

---

## 5. StampedLock

`StampedLock` is a more advanced lock that supports three modes: **read**, **write**, and **optimistic read**. It provides better performance in scenarios with many reads and few writes.
- Optimistic read allows for non-blocking reads, but you must validate the stamp to ensure data consistency.
- If a write occurs during an optimistic read, the read must be retried.
- Use `StampedLock` when you have a high contention scenario with many reads and few writes, and you want to optimize for read performance.
```text
StampedLock lock = new StampedLock();
long stamp = lock.tryOptimisticRead();
try {
    // read data
    if (!lock.validate(stamp)) {
        // fallback to read lock
        stamp = lock.readLock();
        try {
            // read data again
        } finally {
            lock.unlockRead(stamp);
        }
    }
} finally {
    // no need to unlock if optimistic read was successful                  
}
```
---
## 6. Semaphore
`Semaphore` is a counting synchronization aid that restricts access to a resource by a set of threads. It maintains a set of permits, and threads can acquire or release permits.
- Use `Semaphore` when you want to limit the number of threads that can access a resource at the same time, such as a connection pool or a limited resource.

```text
Semaphore semaphore = new Semaphore(3); // 3 permits available
semaphore.acquire(); // acquire a permit            
try {
    // access the resource
} finally {
    semaphore.release(); // release the permit
}
```
---
## Java Concurrency Locks — Grand Comparison Table

| Lock Type               |Multiple Readers |Single Writer |Optimistic Read | Reentrancy |Fairness Option | Try Lock | Interruptible | Permits/Count | Use Case / Notes                                   |
|-------------------------|:---------------:|:------------:|:--------------:|:----------:|:--------------:|:--------:|:-------------:|:-------------:|----------------------------------------------------|
| synchronized            |       No        |     Yes      |       No       |    Yes     |       No       |   No     |      No       |      No       | Basic mutual exclusion, simple, JVM managed        |
| ReentrantLock           |       No        |     Yes      |       No       |    Yes     |      Yes       |   Yes    |     Yes       |      No       | Advanced mutual exclusion, flexible, explicit      |
| ReentrantReadWriteLock  |       Yes       |     Yes      |       No       |    Yes     |      Yes       |   Yes    |     Yes       |      No       | Read-heavy scenarios, separate read/write locks    |
| StampedLock             |       Yes       |     Yes      |      Yes       |    No      |       No       |   Yes    |     Yes       |      No       | High-performance, optimistic reads, not reentrant  |
| Semaphore               |      Yes*       |     Yes*     |       No       |    N/A     |      Yes       |   Yes    |     Yes       |     Yes       | Resource limiting, permits, not for data locking   |
| CountDownLatch          |       N/A       |     N/A      |       No       |    N/A     |       No       |   No     |     Yes       |     Yes       | One-time synchronization, thread coordination      |
| CyclicBarrier           |       N/A       |     N/A      |       No       |    N/A     |       No       |   No     |     Yes       |     Yes       | Reusable barrier for thread groups                 |
| Phaser                  |       N/A       |     N/A      |       No       |    N/A     |       No       |   No     |     Yes       |     Yes       | Flexible, multi-phase thread coordination          |

**Legend:**
- *Multiple Readers/Writers for Semaphore: depends on permits, not read/write distinction
- N/A: Not applicable for this lock type
