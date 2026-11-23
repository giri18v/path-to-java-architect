package com.pathtojavaarchitect.patterns.concurrency.atomicity;

import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentWorkerDemo {

    private static final Logger log = LoggerFactory.getLogger(PaymentWorkerDemo.class);

    static void main() throws InterruptedException {

        var paymentQueue = new LinkedBlockingQueue<String>();
        paymentQueue.add("Payment 1");
        paymentQueue.add("Payment 2");
        paymentQueue.add("Payment 3");

        var paymentWorker = new PaymentWorker(paymentQueue);

        var workerThread = new Thread(paymentWorker,"Payment Worker");
        workerThread.start();

        Thread.sleep(1000);

        paymentWorker.shutdown();
        workerThread.join();

        log.info("Application shutdown completed");
        log.info("Payments remaining in queue: {}",
                 paymentQueue.size());

    }
}
