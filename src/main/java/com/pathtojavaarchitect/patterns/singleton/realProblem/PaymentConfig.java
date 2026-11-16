package com.pathtojavaarchitect.patterns.singleton.realProblem;

public class PaymentConfig {

private static PaymentConfig paymentConfig;


private PaymentConfig() {

}


public static synchronized PaymentConfig getPaymentConfig() {
    if (paymentConfig == null) {
        paymentConfig = new PaymentConfig();
    }
    return paymentConfig;
}


}
