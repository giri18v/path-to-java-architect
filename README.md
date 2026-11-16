# Path to Java Architect

A hands-on learning journey from Java developer to Java architect. This repository
covers **all the classic Gang of Four (GoF) design patterns**: each one is built up
step by step, from a naive implementation to a production-ready version, with
real-world scenarios and JUnit tests that prove the behaviour.

## Goals

- Understand *why* each design pattern exists, not just *how* to write it
- Evolve every pattern incrementally and see the problems each step solves
- Apply patterns to realistic scenarios (payments, configuration, logging, etc.)
- Back every implementation with tests, including concurrency tests where relevant

## Tech Stack

- Java
- Gradle (Kotlin DSL)
- JUnit 5 (Jupiter)

## Project Structure

```
src/
├── main/java/com/pathtojavaarchitect/
│   └── patterns/
│       └── singleton/
│           ├── LazySingleton.java            # Step 1: single-threaded lazy init
│           ├── SingletonMultithreaded.java   # Step 2: synchronized access
│           ├── SingletonWithDCL.java         # Step 3: double-checked locking
│           └── realProblem/
│               ├── PaymentConfig.java        # Real-world: payment configuration
│               ├── PaymentConfigWithDCL.java
│               └── SingletonDemo.java
└── test/java/com/pathtojavaarchitect/patterns/singleton/
    └── ...                                   # Contract + concurrency tests
```

## Design Patterns Roadmap

### Creational Patterns
| Pattern          | Status         |
|------------------|----------------|
| Singleton        | ✅ Done        |
| Factory Method   | ⏳ Planned     |
| Abstract Factory | ⏳ Planned     |
| Builder          | ⏳ Planned     |
| Prototype        | ⏳ Planned     |

### Structural Patterns
| Pattern   | Status     |
|-----------|------------|
| Adapter   | ⏳ Planned |
| Bridge    | ⏳ Planned |
| Composite | ⏳ Planned |
| Decorator | ⏳ Planned |
| Facade    | ⏳ Planned |
| Flyweight | ⏳ Planned |
| Proxy     | ⏳ Planned |

### Behavioral Patterns
| Pattern                 | Status     |
|-------------------------|------------|
| Chain of Responsibility | ⏳ Planned |
| Command                 | ⏳ Planned |
| Interpreter             | ⏳ Planned |
| Iterator                | ⏳ Planned |
| Mediator                | ⏳ Planned |
| Memento                 | ⏳ Planned |
| Observer                | ⏳ Planned |
| State                   | ⏳ Planned |
| Strategy                | ⏳ Planned |
| Template Method         | ⏳ Planned |
| Visitor                 | ⏳ Planned |

## Singleton: Learning Steps

1. **Lazy Singleton (single-threaded):** creates the instance on first use. Simple, but not thread-safe.
2. **Synchronized Singleton:** makes `getInstance()` `synchronized`. Thread-safe, but every call pays the locking cost.
3. **Double-Checked Locking (DCL):** uses a `volatile` instance and locks only during first creation. Thread-safe and fast.
4. **Real-world scenario:** `PaymentConfig`, a single shared payment configuration used across the application.

## Getting Started

### Prerequisites
- JDK 17+ (or the version configured in your IDE)

### Build
```bash
./gradlew build
```

### Run Tests
```bash
./gradlew test
```

## License

This project is for learning purposes.
