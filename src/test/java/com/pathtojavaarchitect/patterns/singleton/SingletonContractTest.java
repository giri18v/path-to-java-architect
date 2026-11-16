package com.pathtojavaarchitect.patterns.singleton;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Shared tests for the thread-safe singletons (synchronized and DCL).
 *
 * Each subclass tests exactly one singleton class. Because the singleton's static field is only
 * initialised on the first getInstance() call, the {@code @Order(1)} test is the first code to touch
 * it, so it really exercises concurrent initialisation, not just retrieval of an existing instance.
 *
 * Do NOT add a subclass for the intentionally unsafe LazySingleton: its race is non-deterministic,
 * so such a test would be flaky. The race is shown separately in LazySingletonDemo.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class SingletonContractTest<T> {

    private static final int THREADS = 64;

    protected abstract T getInstance();

    @Test
    @Order(1)
    void concurrentFirstAccessCreatesOnlyOneInstance() throws Exception {
        Set<T> instances = callConcurrently();
        assertEquals(1, instances.size(), "concurrent first access created more than one instance");
    }

    @Test
    @Order(2)
    void repeatedCallsReturnSameInstance() {
        T first = getInstance();
        for (int i = 0; i < 1_000; i++) {
            assertSame(first, getInstance());
        }
    }

    @Test
    @Order(3)
    void concurrentCallsReturnSameInstance() throws Exception {
        T expected = getInstance();
        Set<T> instances = callConcurrently();
        assertEquals(1, instances.size());
        assertSame(expected, instances.iterator().next());
    }

    /** Releases all threads at the same moment and collects the instances they got (compared by identity). */
    private Set<T> callConcurrently() throws Exception {
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch startGate = new CountDownLatch(1);
        List<Future<T>> futures = new ArrayList<>();

        try (var executor = Executors.newFixedThreadPool(THREADS)) {
            for (int i = 0; i < THREADS; i++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    startGate.await();
                    return getInstance();
                }));
            }
            ready.await(5, TimeUnit.SECONDS); // wait until every thread is parked at the gate
            startGate.countDown();            // release them all at once

            Set<T> instances = Collections.newSetFromMap(new IdentityHashMap<>());
            for (Future<T> future : futures) {
                instances.add(future.get(5, TimeUnit.SECONDS));
            }
            return instances;
        }
    }
}
