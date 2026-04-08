# Java Executor Framework — Quick Guide

---

## 1. The Executor Interface

```java
public interface Executor {
    void execute(Runnable command); // that's literally it
}
```

- Just one method: give it a `Runnable`, it runs it.
- That's the whole contract.

---

## 2. The Family Tree — Simply Explained

```
Executor(interface)
    ↓ extends
ExecutorService(interface)           // run task + manage lifecycle
    (submit, shutdown, get results)
    ↓
ScheduledExecutorService(interface)  // run task after delay or repeatedly

Executors(class)                    // factory for ExecutorService instances

```

- **Executor:** Just runs a task.
- **ExecutorService:** Adds lifecycle management (submit, shutdown, get results).
- **ScheduledExecutorService:** Adds scheduling (delayed or repeated execution).
- **Executors:** Factory class to create different types of ExecutorService instances.
  - Example of ExecutorService instances - `newFixedThreadPool(int nThreads)`, `newCachedThreadPool()`, `newSingleThreadExecutor()`, etc.

---

## 3. Why Use Executors?
- Simplifies thread management.
- Reuses threads (thread pool).
- Handles task scheduling and execution.
- Makes code more readable and maintainable.

---

## 4. Common Implementations

- `ThreadPoolExecutor`: Most common, manages a pool of threads.
- `ScheduledThreadPoolExecutor`: For scheduled and periodic tasks.
- `SingleThreadExecutor`: One thread, tasks run sequentially.
---

## 5. Example Usage
- 
```java
ExecutorService pool = Executors.newFixedThreadPool(4);
pool.execute(() -> System.out.println("Hello from pool!"));
pool.shutdown();
```
---

## 6. Runnable vs Callable
| Feature               | Runnable                | Callable     |
|-----------------------|-------------------------|--------------|
| Return value          | ❌ No                    | ✅ Yes        |
| Exception             | ❌ Cannot throw checked  | ✅ Can throw  |
| Method                | `run()`                 | `call()`     |
| Introduced            | Java 1.0                | Java 5       |
| Used with Future      | ❌ No                    | ✅ Yes        |
