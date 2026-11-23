package com.pathtojavaarchitect.patterns.concurrency.atomicity;

import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentWorker implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(PaymentWorker.class);
    private final BlockingQueue<String> paymentsQueue;

    private volatile boolean running = true;

    public PaymentWorker(BlockingQueue<String> paymentsQueue) {
        this.paymentsQueue = paymentsQueue;
    }

    @Override
    public void run() {
        log.info("Payment worker started");
        while(running){
            try {
                String paymentId = paymentsQueue.poll(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                if(paymentId != null){
                    processPayment(paymentId);
                }
            } catch (InterruptedException e) {
                log.error("Payment worker interrupted", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.info("Payment worker finished");

    }

    private void processPayment(String paymentId) throws InterruptedException{
        log.info("Started Processing {}", paymentId);
        Thread.sleep(1000);
        log.info("Payment Process Completed for  {}", paymentId);
    }
    public void shutdown(){
        running = false;
        log.info("Payment worker shutdown");
    }
}
