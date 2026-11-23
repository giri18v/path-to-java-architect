
package com.pathtojavaarchitect.patterns.concurrency.atomicity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PaymentCounterDemo {

    private static final Logger log =
        LoggerFactory.getLogger(PaymentCounterDemo.class);

    static void main()
        throws InterruptedException, ExecutionException {

        PaymentCounter counter = new PaymentCounter();
        try (var executor = Executors.newFixedThreadPool(5)) {
            var futures = new ArrayList<Future<Integer>>();
            for (int i = 0; i < 100; i++) {
                int paymentId = i + 1;
                Future<Integer> future = executor.submit(
                    () -> {
                        int total = counter.paymentCompleted();
                        log.info(
                            "Payment {} completed. Counter: {}",
                            paymentId,
                            total
                        );
                        return total;
                    }
                );
                futures.add(future);
            }

            for (Future<Integer> future : futures) {
                future.get();
            }
        }

        log.info("Expected payments: {}", 100);
        log.info("Actual payments: {}",
                 counter.getSuccessfulPayment());
    }
}
