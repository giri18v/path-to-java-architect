package com.pathtojavaarchitect.patterns.singleton.realProblem;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SingletonDemo {

     static void main(String[] args) throws InterruptedException {

         try(var executor = Executors.newFixedThreadPool(100)) {

             var tasks = IntStream.range(0,100)
                 .<Callable<PaymentConfig>>mapToObj(i -> (Callable<PaymentConfig>) PaymentConfig::getPaymentConfig)
                 .toList();
             long start = System.nanoTime();
             executor.invokeAll(tasks);
             long end = System.nanoTime();
             System.out.println("synchronized - total time incl. scheduling overhead : "+(end- start)/1_000_000 + " milliseconds");


         }

         try(var executor = Executors.newFixedThreadPool(100)) {

             var tasks = IntStream.range(0,100)
                 .<Callable<PaymentConfigWithDCL>>mapToObj(i -> (Callable<PaymentConfigWithDCL>) PaymentConfigWithDCL::getPaymentConfig)
                 .toList();
             long start = System.nanoTime();
             executor.invokeAll(tasks);
             long end = System.nanoTime();
             System.out.println("DCL          - total time incl. scheduling overhead : "+(end- start)/1_000_000 + " milliseconds");


         }
    }
}
