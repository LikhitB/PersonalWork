## Volatile , Synchronized threading
- Each thread can cache variables in CPU registers or L1/L2 cache. T1 may be reading its own cached copy of `lock` variable and never see the update that main thread wrote to main memory.
- `volatile` tells the JVM — never cache this variable. Always read from and write to main memory directly. Every thread sees the latest value immediately.
- `Synchronised` keyword ensures the threads are locked and are running in synchrounous way.