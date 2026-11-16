package com.pathtojavaarchitect.patterns.singleton.realProblem;

public class PaymentConfigWithDCL {

    private static volatile PaymentConfigWithDCL instance;

    private PaymentConfigWithDCL() {

    }

    public static PaymentConfigWithDCL getPaymentConfig() {
        if(instance == null) {
            synchronized(PaymentConfigWithDCL.class) {
                if(instance == null) {
                    instance = new PaymentConfigWithDCL();
                }
            }
        }
        return instance;
    }
}
