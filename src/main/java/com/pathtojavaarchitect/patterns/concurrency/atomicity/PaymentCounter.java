package com.pathtojavaarchitect.patterns.concurrency.atomicity;

import java.util.concurrent.atomic.AtomicInteger;

public class PaymentCounter {

    private final AtomicInteger counter = new AtomicInteger(0);


    public int paymentCompleted() {
        return counter.incrementAndGet();
    }

    public int getSuccessfulPayment() {
        return counter.get();
    }

}
