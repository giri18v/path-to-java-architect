package com.pathtojavaarchitect.patterns.concurrency.atomicity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentWorkerTest {

    /** Worker polls every 500 ms and processes a payment in ~1000 ms. */
    private static final long JOIN_TIMEOUT_MS = 3_000;

    private Thread workerThread;

    @AfterEach
    void tearDown() throws InterruptedException {
        if (workerThread != null && workerThread.isAlive()) {
            workerThread.interrupt();
            workerThread.join(JOIN_TIMEOUT_MS);
        }
    }

    private Thread start(Runnable runnable) {
        workerThread = new Thread(runnable, "Payment Worker (test)");
        workerThread.start();
        return workerThread;
    }

    /** Busy-waits (with small sleeps) until the queue reaches the given size. */
    private static void awaitQueueSize(BlockingQueue<?> queue, int size) throws InterruptedException {
        long deadline = System.currentTimeMillis() + JOIN_TIMEOUT_MS;
        while (queue.size() != size) {
            if (System.currentTimeMillis() > deadline) {
                throw new AssertionError("Queue never reached size " + size + ", was " + queue.size());
            }
            Thread.sleep(10);
        }
    }

    @Test
    @DisplayName("Idle worker stops promptly after shutdown (volatile flag is visible)")
    void idleWorkerStopsAfterShutdown() throws InterruptedException {
        PaymentWorker worker = new PaymentWorker(new LinkedBlockingQueue<>());
        Thread thread = start(worker);

        Thread.sleep(100); // let it enter the poll loop
        worker.shutdown();
        thread.join(JOIN_TIMEOUT_MS);

        assertFalse(thread.isAlive(), "worker should observe running=false and exit");
    }

    @Test
    @DisplayName("Shutdown lets the in-flight payment finish and leaves the rest in the queue")
    void shutdownFinishesCurrentPaymentAndLeavesRemaining() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("Payment 1");
        queue.add("Payment 2");
        queue.add("Payment 3");

        PaymentWorker worker = new PaymentWorker(queue);
        Thread thread = start(worker);

        awaitQueueSize(queue, 2); // "Payment 1" taken, now processing (~1s)
        worker.shutdown();
        thread.join(JOIN_TIMEOUT_MS);

        assertFalse(thread.isAlive());
        assertEquals(2, queue.size(), "only the in-flight payment should be consumed");
        assertEquals("Payment 2", queue.peek());
    }

    @Test
    @DisplayName("Worker drains all queued payments while running")
    void workerDrainsQueueWhileRunning() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("Payment 1");
        queue.add("Payment 2");

        PaymentWorker worker = new PaymentWorker(queue);
        Thread thread = start(worker);

        awaitQueueSize(queue, 0);
        worker.shutdown();
        thread.join(JOIN_TIMEOUT_MS);

        assertFalse(thread.isAlive());
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("Worker picks up payments added after it started")
    void workerPicksUpLatePayments() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        PaymentWorker worker = new PaymentWorker(queue);
        start(worker);

        Thread.sleep(100);
        queue.add("Late Payment");

        awaitQueueSize(queue, 0);
        worker.shutdown();
    }

    @Test
    @DisplayName("Interrupt stops the worker and restores the interrupt flag")
    void interruptStopsWorkerAndRestoresFlag() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("Payment 1"); // worker will be sleeping inside processPayment

        PaymentWorker worker = new PaymentWorker(queue);
        AtomicBoolean interruptFlagAfterRun = new AtomicBoolean(false);
        Thread thread = start(() -> {
            worker.run();
            interruptFlagAfterRun.set(Thread.currentThread().isInterrupted());
        });

        awaitQueueSize(queue, 0);
        thread.interrupt();
        thread.join(JOIN_TIMEOUT_MS);

        assertFalse(thread.isAlive(), "worker should exit on interrupt without shutdown()");
        assertTrue(interruptFlagAfterRun.get(), "worker should re-set the interrupt flag");
    }

    @Test
    @DisplayName("PaymentWorkerDemo runs to completion without errors")
    void demoMainRuns() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), PaymentWorkerDemo::main);
    }
}
