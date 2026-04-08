# Java String Features and Concepts

---

## Java 11 String Methods

| Method / Example                | Description                        | Output / Notes           |
|---------------------------------|------------------------------------|-------------------------|
| `"  hello  ".strip()`           | Unicode-aware trim                 | `"hello"`               |
| `"   ".isBlank()`               | Checks if string is blank          | `true`                  |
| `"a\nb\nc".lines()`             | Splits into lines (Stream<String>) | `a`, `b`, `c`           |
| `"ha".repeat(3)`                | Repeats string                     | `"hahaha"`              |

---

## Java 12 String Indentation

```java
String result = "hello world"
    .indent(4); // adds 4 spaces to each line
```

---

## Java 15 — Text Blocks (Stable)

```java
String html = """
<html>
<body>Hello</body>
</html>
""";
```

---

## Java 21 — String Templates *(Preview, use STR processor)*

```java
String msg = STR."Hello, \{name}! You have \{count} messages.";
```

---

## Autoboxing and Unboxing

| Primitive Type | Wrapper Class   |
|---------------|----------------|
| `int`         | `Integer`      |
| `char`        | `Character`    |
| `double`      | `Double`       |
| `boolean`     | `Boolean`      |
| `byte`        | `Byte`         |
| `short`       | `Short`        |
| `long`        | `Long`         |
| `float`       | `Float`        |

- **Primitive to Wrapper** is called **Autoboxing**

### Example

```java
String s = "a";
// 'a' is a char type, so Java internally autoboxes it to the Wrapper class
// Internally, it does String.valueOf('a') and assigns
// This process is called Autoboxing.
```

---

### Unboxing Example

```java
Integer num = 10; // Autoboxing: int to Integer internally java does this Integer.valueOf(10) 
int n = num;      // Unboxing: Integer to int internally java does num.intValue()
// Java automatically converts the Integer object to a primitive int
```
---

