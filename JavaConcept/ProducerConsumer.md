# 🧵 Java Thread Communication — Producer & Consumer Pattern

## 📌 Table of Contents
- [What is Thread Communication?](#what-is-thread-communication)
- [The Problem Without Communication](#the-problem-without-communication)
- [Key Methods](#key-methods)
- [How wait() and notify() Work](#how-wait-and-notify-work)
- [Producer Consumer — Full Example](#producer-consumer--full-example)
- [Critical Rules](#critical-rules)
- [Spurious Wakeup](#spurious-wakeup)
- [notifyAll() vs notify()](#notifyall-vs-notify)
- [Common Mistakes](#common-mistakes)
- [Quick Summary](#quick-summary)

---

## What is Thread Communication?

Thread communication is the mechanism that allows threads to **coordinate with each other** — one thread pauses and waits until another thread signals it to continue.

Without this, threads run independently and blindly. With it, they can **hand off work** to each other in an organized way.

> **Real world analogy:** A chef (Producer) prepares a dish and places it on the counter. The waiter (Consumer) waits at the counter. Once the dish is ready, the chef rings a bell — the waiter picks it up and serves it. The waiter doesn't keep checking the counter every second; they **wait** for the bell.

---

## The Problem Without Communication

```java
// ❌ BAD — Busy waiting (polling) wastes CPU
while (!dataReady) {
    // spinning doing nothing, burning CPU cycles
}
```

This is called **busy waiting** or **spin waiting**. The thread consumes 100% CPU just checking a condition repeatedly. Thread communication with `wait()` and `notify()` solves this properly.

---

## Key Methods

All three methods belong to `java.lang.Object` — meaning **every Java object** can be used as a communication channel.

| Method           | What it does                                                       |
|------------------|--------------------------------------------------------------------|
| `wait()`         | Releases the lock and suspends the current thread until notified   |
| `wait(long ms)`  | Same as wait() but wakes up automatically after given milliseconds |
| `notify()`       | Wakes up **one** randomly chosen thread waiting on this object     |
| `notifyAll()`    | Wakes up **all** threads waiting on this object                    |

> ⚠️ All three methods **must** be called inside a `synchronized` block, otherwise `IllegalMonitorStateException` is thrown at runtime.

---

## How wait() and notify() Work

```
Thread A (Consumer)                     Thread B (Producer)
──────────────────                      ────────────────────
enters synchronized(lock)               
calls lock.wait()
  → releases the lock          
  → goes to WAITING state      ──────►  enters synchronized(lock)
                                         does work, sets data
                                         calls lock.notify()
                                         exits synchronized block
  ← wakes up, tries to                
    reacquire lock              ◄──────  lock released
  → goes to BLOCKED state               
  → acquires lock               
  → continues execution         
```

### Thread State Flow

```
NEW
 │
 ▼
RUNNABLE ──── calls wait() ────► WAITING
    ▲                                │
    │                          notify() called
    │                                │
    └──── reacquires lock ◄── BLOCKED (waiting for lock)
```

## Quick Summary

```
wait()      → release lock + pause thread (goes to WAITING)
notify()    → wake one waiting thread (goes to BLOCKED → RUNNABLE)
notifyAll() → wake all waiting threads

Must be inside synchronized block        ✅
Always use while loop with wait()        ✅
Restore interrupt flag in catch block    ✅
Use same lock object in both threads     ✅
```

### The Golden Template

```java
// CONSUMER
synchronized (lock) {
    while (!conditionMet) {
        lock.wait();
    }
    // do work
}

// PRODUCER
synchronized (lock) {
    // do work
    conditionMet = true;
    lock.notify(); // or notifyAll()
}
```

---

