package com.pathtojavaarchitect.patterns.singleton;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Single-threaded only. LazySingleton is intentionally not thread-safe, and its race is
 * non-deterministic, so a concurrent test here would be flaky. See LazySingletonDemo instead.
 */
class LazySingletonTest {

    @Test
    void repeatedCallsOnOneThreadReturnSameInstance() {
        LazySingleton first = LazySingleton.getInstance();
        for (int i = 0; i < 1_000; i++) {
            assertSame(first, LazySingleton.getInstance());
        }
    }
}
